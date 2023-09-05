package com.example.gg.member.controller;


import com.example.gg.member.domain.model.Member;
import com.example.gg.member.service.SignService;
import com.example.gg.member.dto.LoginRequest;
import com.example.gg.member.dto.SignRequest;
import com.example.gg.member.dto.SignResponse;
import com.example.gg.security.TokenDto;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name="인증", description = "로그인, 회원가입, 정보 조회(user, admin), 토큰 리프레시(access, refresh 둘다)")
@RestController
@RequiredArgsConstructor
@Slf4j
//@EnableAutoConfiguration(exclude = {org.springframework.boot.autoconfigure.gson.GsonAutoConfiguration.class})
public class SignController {

    private final SignService memberService;

    private String get_access_token(String token){
        String[] splitJwt = token.split(" ");
        return splitJwt[1]; // "Bearer "제거
    }

    @Operation(summary = "로그인", description = "로그인 메서드입니다.")
    @PostMapping(value = "/api/login")
    public ResponseEntity<SignResponse> login(@RequestBody LoginRequest request) throws Exception {
        return new ResponseEntity<>(memberService.login(request), HttpStatus.OK);
    }

    @Operation(summary = "회원가입", description = "회원가입 메서드입니다.")
    @PostMapping(value = "/api/signup")
    public ResponseEntity<Member> signup(@RequestBody SignRequest request) throws Exception {
        return new ResponseEntity<>(memberService.register(request), HttpStatus.OK);
    }

    @Operation(summary = "유저 정보 조회",description = "유저 정보 조회 메서드입니다.")
    @SecurityRequirement(name="access-token")
    @GetMapping("/user/get")
    public ResponseEntity<SignResponse> getUser(@RequestHeader(value = "Authorization") String token) throws Exception {
        String access_token = get_access_token(token);
        return new ResponseEntity<>( memberService.getMember(access_token), HttpStatus.OK);
    }

    @Operation(summary = "어드민 정보 조회", description = "어드민 정보 조회 메서드입니다.")
    @SecurityRequirement(name="access-token")
    @GetMapping("/admin/get")
    public ResponseEntity<SignResponse> getUserForAdmin(@RequestHeader(value = "Authorization") String token) throws Exception {
        String access_token = get_access_token(token);
        return new ResponseEntity<>( memberService.getMember(access_token), HttpStatus.OK);
    }

    //주석추가
    @Operation(summary = "토큰 리프레시", description = "access, refresh 토큰 모두 갱신합니다.")
    @SecurityRequirement(name="access-token")
    @PostMapping("/user/refresh")
    public ResponseEntity<TokenDto> refresh(@RequestBody TokenDto token) throws Exception {
        return new ResponseEntity<>( memberService.refreshAccessToken(token), HttpStatus.OK);
    }

    @Operation(summary = "게임 닉네임 추가", description = ".")
    @SecurityRequirement(name="access-token")
    @PostMapping("/user/add/gameName")
    public void add_nicks(@RequestHeader(value = "Authorization") String token, String nicks){
        String access_token = get_access_token(token);
        memberService.add_nick(access_token, nicks);
    }

    @Operation(summary = "게임 닉네임 리스트 가져오기", description = ".")
    @SecurityRequirement(name="access-token")
    @PostMapping("/user/list")
    public List<String> list(@RequestHeader(value = "Authorization") String token){
        String access_token = get_access_token(token);
        return memberService.list(access_token);
    }

    @Operation(summary = "게임 닉네임 모스트 설정", description = "1:1 매핑으로 새로운걸 연결해주면 원래 있던건 자동 해제됨")
    @SecurityRequirement(name="access-token")
    @PostMapping("/user/set/most")
    public ResponseEntity set_most(@RequestHeader(value = "Authorization") String token, String nick){
        String access_token = get_access_token(token);
        try{
            memberService.set_most(access_token, nick);
            return ResponseEntity.ok(HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }



//    @Operation(summary = "게임 닉네임 추가", description = "true/false, access token필요")
//    @PostMapping("/user/setNickname")
//    public ResponseEntity<Boolean> setGameNickname(String access_token, String nickname) throws Exception {
//        return new ResponseEntity<>(memberService.set_game_nickname(access_token, nickname), HttpStatus.OK);
//    }

//    @PostMapping("/user/setNickname")
//    public void setGameNickname(String access_token, String gameName) throws Exception {
//        memberService.set_gameName(access_token, gameName);
//    }




}