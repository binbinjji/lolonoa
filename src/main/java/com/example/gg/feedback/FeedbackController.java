package com.example.gg.feedback;

import com.example.gg.feedback.domain.model.Feedback;
import com.example.gg.feedback.dto.FeedbackAddDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/feedback")
@RequiredArgsConstructor
@Tag(name="피드백", description = "추가, 삭제")
public class FeedbackController {

    private final FeedbackService feedbackService;

    private String get_access_token(String token){
        String[] splitJwt = token.split(" ");
        return splitJwt[1]; // "Bearer "제거
    }

    @Operation(summary = "해당 롤문철의 피드백 불러오기", description = "아직 테스트 x")
    @GetMapping("/all")
    public Page<Feedback> feedbacks_list(@PageableDefault(size = 12, sort = "id",  direction = Sort.Direction.DESC) Pageable pageable, @RequestParam Long judgement_id){
        return feedbackService.feedbacks_list(pageable, judgement_id);
    }

    @Operation(summary = "피드백 추가", description = "..")
    @PostMapping("/add")
    public ResponseEntity<Feedback> add_feedback(@RequestHeader(value = "Authorization") String token, @RequestBody FeedbackAddDTO feedbackAddDTO){
        String access_token = get_access_token(token);
        return new ResponseEntity<>(feedbackService.add_feedback(access_token, feedbackAddDTO), HttpStatus.OK);
    }

    @Operation(summary = "피드백 삭제", description = "..")
    @DeleteMapping("/delete/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void delete_feedback(@RequestHeader(value = "Authorization") String token, @PathVariable Long id){
        String access_token = get_access_token(token);
        feedbackService.delete_feedback(access_token,id);
    }
}
