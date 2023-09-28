package com.example.gg.member.service;

import com.example.gg.member.domain.model.Nicks;
import com.example.gg.member.repository.MemberRepository;
import com.example.gg.member.domain.model.Authority;
import com.example.gg.member.domain.model.Member;
import com.example.gg.member.dto.LoginRequest;
import com.example.gg.member.dto.SignRequest;
import com.example.gg.member.dto.SignResponse;
import com.example.gg.member.repository.NicksRepository;
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

import java.util.*;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class SignService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;
    private final TokenRepository tokenRepository;
    private final NicksRepository nicksRepository;

    public SignResponse login(LoginRequest request) throws Exception {

        Member member = memberRepository.findByAccount(request.getAccount()).orElseThrow(() ->
                new BadCredentialsException("잘못된 계정정보입니다."));

        if (!passwordEncoder.matches(request.getPassword(), member.getPassword())) {
            throw new BadCredentialsException("잘못된 계정정보입니다.");
        }

        return SignResponse.builder()
                .id(member.getId())
                .account(member.getAccount())
                .nickname(member.getNickname())
                .roles(member.getRoles())
                .token(TokenDto.builder()
                        .access_token(jwtProvider.createToken(member.getAccount(), member.getRoles()))
                        .refresh_token(createRefreshToken(member))
                        .build())
                .build();
    }

    public Member register(SignRequest request) throws Exception {
        try {
            Member member = Member.builder()
                    .account(request.getAccount())
                    .password(passwordEncoder.encode(request.getPassword()))
                    .nickname(request.getNickname())
                    .build();

            member.setRoles(Collections.singletonList(Authority.builder().name("ROLE_USER").build()));

            Member savedMember = memberRepository.save(member);
            return savedMember;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new Exception("잘못된 요청입니다.");
        }
    }

    public SignResponse getMember(String access_token) throws Exception {
        String account = jwtProvider.getAccount(access_token);
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

    public boolean checkEmailDuplicate(String account){
        if(memberRepository.existsByAccount(account)){
            return true;
        }
        return false;
    }

    public void add_nick(String access_token, String nicks){
        String account = jwtProvider.getAccount(access_token);
        Member member = memberRepository.findByAccount(account).get();
        Nicks nick = nicksRepository.save(
                Nicks.builder()
                        .nick(nicks)
                        .member(member)
                        .build()
        );
        member.add_Nicks(nick);
    }

    public List<String> list(String access_token){
        String account = jwtProvider.getAccount(access_token);
        Member member = memberRepository.findByAccount(account).get();
        List<Nicks> nicks = member.getNicks();

        List<String> arrayList = new ArrayList<String>();
        for (Nicks nick : nicks) {
            arrayList.add(nick.getNick());
        }
        return arrayList;
    }

    public void set_most(String access_token, String nick){
        String account = jwtProvider.getAccount(access_token);
        Optional<Member> member = memberRepository.findByAccount(account);
        if(member.isPresent()){
            Nicks nicks = nicksRepository.findByNick(nick);
            member.get().setMost(nicks);
        }else{
            throw new IllegalArgumentException("잘못된 요청입니다(존재하지않는 닉네임)");
        }

    }



//    public void add_gameName(String access_token, String gameName){
//        String account = jwtProvider.getAccount(access_token);
//        Member member = memberRepository.findByAccount(account).get();
//
//        GameName new_gameName = GameName.builder()
//                .member(member)
//                .gameName(gameName)
//                .build();
//        gameNameRepository.save(new_gameName);
////        member.addGameName(new_gameName);
//    }

//    public void set_most(String access_token, String gameName){
//        String account = jwtProvider.getAccount(access_token);
//        Member member = memberRepository.findByAccount(account).get();
//
//        GameName find_GameName = gameNameRepository.findByGameName(gameName);
//        find_GameName.setMost(member);
//        member.setMost(find_GameName);
//
//    }




//    public boolean set_gameName(String access_token, String gameName){
//        String account = jwtProvider.getAccount(access_token);
//        Optional<Member> member = memberRepository.findByAccount(account);
//
//        if(member.isPresent()){
//            GameName gameName1 = GameName.builder()
//                    .gameName(gameName)
//                    .build();
//
//            member.get().setGameName(Collections.singletonList(gameName1));
//            return true;
//        }
//        return false;
//    }



}
