package com.example.gg.member;

import com.example.gg.member.dto.LoginRequest;
import com.example.gg.member.dto.SignRequest;
import com.example.gg.member.dto.SignResponse;
import com.example.gg.security.JwtProvider;
import com.example.gg.security.Token;
import com.example.gg.security.TokenDto;
import com.example.gg.security.TokenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class SignService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;
    private final TokenRepository tokenRepository;

    public SignResponse login(LoginRequest request) throws Exception {
        Member member = memberRepository.findByAccount(request.getAccount()).orElseThrow(() ->
                new BadCredentialsException("잘못된 계정정보입니다."));

        if (!passwordEncoder.matches(request.getPassword(), member.getPassword())) {
            throw new BadCredentialsException("잘못된 계정정보입니다.");
        }

        return SignResponse.builder()
                .id(member.getId())
                .account(member.getAccount())
                .email(member.getEmail())
                .roles(member.getRoles())
                .token(TokenDto.builder()
                        .access_token(jwtProvider.createToken(member.getAccount(), member.getRoles()))
                        .refresh_token(createRefreshToken(member))
                        .build())
                .build();
    }

    public boolean register(SignRequest request) throws Exception {
        try {
            Member member = Member.builder()
                    .account(request.getAccount())
                    .password(passwordEncoder.encode(request.getPassword()))
                    .email(request.getEmail())
                    .build();

            member.setRoles(Collections.singletonList(Authority.builder().name("ROLE_USER").build()));

            memberRepository.save(member);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new Exception("잘못된 요청입니다.");
        }
        return true;
    }

    public SignResponse getMember(String account) throws Exception {
        Member member = memberRepository.findByAccount(account)
                .orElseThrow(() -> new Exception("계정을 찾을 수 없습니다."));
        return new SignResponse(member);
    }



    public String createRefreshToken(Member member){
        Token token = tokenRepository.save(
                Token.builder()
                        .id(member.getId())
                        .refresh_token(UUID.randomUUID().toString())
                        .expiration(60*60*2) // 14일 =60*60*24*14
                        .build()
        );
        return token.getRefresh_token();
    }

    public Token validRefreshToken(Member member, String refreshToken) throws Exception {
        Token token = tokenRepository.findById(member.getId()).orElseThrow(() -> new Exception("만료된 계정입니다. 로그인을 다시 시도하세요"));
        // 해당유저의 Refresh 토큰 만료 : Redis에 해당 유저의 토큰이 존재하지 않음
        if (token.getRefresh_token() == null) {
            return null;
        } else {
            // 토큰이 같은지 비교
            if(!token.getRefresh_token().equals(refreshToken)) {
                return null;
            } else {
                return token;
            }
        }
    }

    public TokenDto refreshAccessToken(TokenDto token) throws Exception {
        String account = jwtProvider.getAccount(token.getAccess_token());
        Member member = memberRepository.findByAccount(account).orElseThrow(() ->
                new BadCredentialsException("잘못된 계정정보입니다."));
        Token refreshToken = validRefreshToken(member, token.getRefresh_token());

        if (refreshToken != null) {

            /*리프레시 토큰 값 변경*/
            Token findToken = tokenRepository.findById(member.getId()).get();
            findToken.setRefresh_token(UUID.randomUUID().toString());
            findToken.setExpiration(60*60*2);
            tokenRepository.save(findToken);

            return TokenDto.builder()
                    .access_token(jwtProvider.createToken(account, member.getRoles()))
                    .refresh_token(findToken.getRefresh_token())
                    .build();
        } else {
            throw new Exception("로그인을 해주세요");
        }
    }
}
