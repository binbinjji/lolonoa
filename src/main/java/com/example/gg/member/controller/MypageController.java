package com.example.gg.member.controller;

import com.example.gg.judgement.JudgementService;
import com.example.gg.review.ReviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/mypage")
@Slf4j
public class MypageController {
    private final ReviewService reviewService;
    private final JudgementService judgementService;

    private String get_access_token(String token){
        String[] splitJwt = token.split(" ");
        return splitJwt[1]; // "Bearer "제거
    }

    @Operation(summary = "본인이 남긴 리뷰 리스트 가져오기", description = "..")
    @SecurityRequirement(name="access-token")
    @GetMapping("/review")
    public ResponseEntity review_list(@RequestHeader(value = "Authorization") String token, @PageableDefault(size = 10) Pageable pageable){
        String access_token = get_access_token(token);
        return new ResponseEntity<>(reviewService.reviews_by_member(pageable, access_token), HttpStatus.OK);
    }

    @Operation(summary = "본인이 남긴 롤문철 리스트 가져오기", description = "..")
    @SecurityRequirement(name="access-token")
    @GetMapping("/judgement")
    public ResponseEntity judgement_list(@RequestHeader(value = "Authorization") String token, @PageableDefault(size = 10) Pageable pageable){
        String access_token = get_access_token(token);
        return new ResponseEntity<>(judgementService.judgements_by_member(pageable, access_token), HttpStatus.OK);
    }
}
