package com.example.gg.member;


import com.example.gg.member.dto.LoginRequest;
import com.example.gg.member.dto.SignRequest;
import com.example.gg.member.dto.SignResponse;
import com.example.gg.security.TokenDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name="인증", description = "로그인, 회원가입, 정보 조회(user, admin), 토큰 리프레시(access, refresh 둘다)")
@RestController
@RequiredArgsConstructor
public class SignController {

    private final SignService memberService;

    @Operation(summary = "로그인", description = "로그인 메서드입니다.")
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "200", description = "로그인 성공", content = @Content(schema = @Schema(implementation = Member.class))),
//            @ApiResponse(responseCode = "401", description = "Bad Request", content = @Content(schema = @Schema(implementation = Error.class))),
//
//    })
    @PostMapping(value = "/user/login")
    public ResponseEntity<SignResponse> login(@RequestBody LoginRequest request) throws Exception {
        return new ResponseEntity<>(memberService.login(request), HttpStatus.OK);
    }

    @Operation(summary = "회원가입", description = "회원가입 메서드입니다.")
    @PostMapping(value = "/user/signup")
    public ResponseEntity<Boolean> signup(@RequestBody SignRequest request) throws Exception {
        return new ResponseEntity<>(memberService.register(request), HttpStatus.OK);
    }

    @Operation(summary = "유저 정보 조회",description = "유저 정보 조회 메서드입니다.")
    @SecurityRequirement(name="access-token")
    @GetMapping("/user/get")
    public ResponseEntity<SignResponse> getUser(@RequestParam String account) throws Exception {
        return new ResponseEntity<>( memberService.getMember(account), HttpStatus.OK);
    }

    @Operation(summary = "어드민 정보 조회", description = "어드민 정보 조회 메서드입니다.")
    @GetMapping("/admin/get")
    public ResponseEntity<SignResponse> getUserForAdmin(@RequestParam String account) throws Exception {
        return new ResponseEntity<>( memberService.getMember(account), HttpStatus.OK);
    }

    //주석추가
    @Operation(summary = "토큰 리프레시", description = "access, refresh 토큰 모두 갱신합니다.")
    @GetMapping("/user/refresh")
    public ResponseEntity<TokenDto> refresh(@RequestBody TokenDto token) throws Exception {
        return new ResponseEntity<>( memberService.refreshAccessToken(token), HttpStatus.OK);
    }
}