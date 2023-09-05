package com.example.gg.member.controller;

import com.example.gg.member.dto.VerifyRequest;
import com.example.gg.member.service.MailService;
import com.example.gg.member.service.SignService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name="이메일", description = "인증번호 전송, 비교(확인) + 타임아웃은 5분이다.")
@RestController
@RequiredArgsConstructor
public class MailController {
    private final MailService mailService;

    private final SignService memberService;

    @Operation(summary = "이메일 인증번호 전송", description = "true/false. 추가로 인증코드를 보내는 경우, redis에서 자동으로 해당 키 값의 인증코드가 갱신된다.")
    @PostMapping("/email/send")
    public ResponseEntity<Boolean> send(@RequestParam String email){
        return new ResponseEntity<>(mailService.sendEmail(email), HttpStatus.OK);
    }

    @Operation(summary = "이메일 인증번호 비교(확인)", description = "true/false")
    @PostMapping("/email/verify")
    public ResponseEntity<Boolean> verify(@RequestBody VerifyRequest verifyRequest) throws Exception {
        return new ResponseEntity<>(mailService.verifyCode(verifyRequest), HttpStatus.OK);
    }

    @Operation(summary = "이메일 중복체크", description = "이미 존재하면 true, 없으면 false")
    @GetMapping("/email/duplicate")
    public ResponseEntity<Boolean> checkDuplicate(@RequestParam String account){
        return new ResponseEntity<>(memberService.checkEmailDuplicate(account), HttpStatus.OK);
    }
}
