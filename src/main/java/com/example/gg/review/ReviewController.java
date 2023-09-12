package com.example.gg.review;

import com.example.gg.review.domain.model.Review;
import com.example.gg.review.dto.ReviewAddDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/review")
@RequiredArgsConstructor
@Tag(name="리뷰", description = "추가, 삭제(아직), 편집(아직)")
public class ReviewController {

    private final ReviewService reviewService;

    private String get_access_token(String token){
        String[] splitJwt = token.split(" ");
        return splitJwt[1]; // "Bearer "제거
    }

    @Operation(summary = "리뷰 추가", description = "..")
    @SecurityRequirement(name="access-token")
    @PostMapping("/add")
    public ResponseEntity<Review> add_review(@RequestHeader(value = "Authorization") String token, @RequestBody ReviewAddDTO reviewAddDTO){
        String access_token = get_access_token(token);
        return new ResponseEntity<>(reviewService.add_review(access_token,reviewAddDTO), HttpStatus.OK);
    }

    @Operation(summary = "리뷰 삭제", description = "..")
    @SecurityRequirement(name="access-token")
    @DeleteMapping("/delete/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete_review(@RequestHeader(value = "Authorization") String token, @PathVariable Long id){
        String access_token = get_access_token(token);
        reviewService.delete_review(access_token,id);
    }


    @Operation(summary = "리뷰 리스트", description = "닉네임으로 조회")
    @ResponseBody
    @GetMapping("/list")
    public ResponseEntity reviews_list(@RequestParam String nickname){
        return new ResponseEntity<>(reviewService.reviews_list(nickname), HttpStatus.OK);
    }

//    @ResponseBody
//    @GetMapping("/list/{game_id}")
//    public List<String> list(@PathVariable Long game_id){
//        return reviewService.list(game_id);
//    }
}
