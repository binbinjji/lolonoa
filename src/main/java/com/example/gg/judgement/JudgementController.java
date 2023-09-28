package com.example.gg.judgement;

import com.example.gg.judgement.domain.model.Judgement;
import com.example.gg.judgement.dto.JudgementAddDTO;
import com.example.gg.judgement.dto.JudgementUpdateDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/judgement")
@RequiredArgsConstructor
@Tag(name="롤문철", description = "추가, 삭제")
public class JudgementController {

    private final JudgementService judgementService;

    private String get_access_token(String token){
        String[] splitJwt = token.split(" ");
        return splitJwt[1]; // "Bearer "제거
    }

    @Operation(summary = "롤문철 리스트 조회", description = "파라미터 ex) page=0")
    @GetMapping("/list")
    @ResponseBody
    public Page<Judgement> judgements(@PageableDefault(size = 10)Pageable pageable){
        return judgementService.judgements_all(pageable);
    }

    @Operation(summary = "롤문철 추가", description = "성공시 Judgement 반환")
    @PostMapping("/add")
    public ResponseEntity<Judgement> add_judgement(@RequestHeader(value = "Authorization") String token, @RequestBody JudgementAddDTO judgementAddDTO){
        String access_token = get_access_token(token);
        return new ResponseEntity<>(judgementService.add_judgement(access_token, judgementAddDTO), HttpStatus.OK);
    }

    @Operation(summary = "롤문철 조회", description = "..")
    @GetMapping("/search/{id}")
    public ResponseEntity<Judgement> search_judgement(@PathVariable Long id){
        return new ResponseEntity<>(judgementService.search_judgement(id), HttpStatus.OK);
    }

    @Operation(summary = "롤문철 삭제", description = "반환 값 없음(필요하면 말 ㄱㄱ)")
    @DeleteMapping("/delete/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete_judgement(@RequestHeader(value = "Authorization") String token, @PathVariable Long id){
        String access_token = get_access_token(token);
        judgementService.delete_judgement(access_token, id);
    }

    @Operation(summary = "롤문철 수정", description = "dto중 변경된 값만 업데이트")
    @PutMapping("/update/{id}")
    public ResponseEntity<Judgement> update_judgement(@RequestHeader(value = "Authorization") String token, @PathVariable Long id, @RequestBody JudgementUpdateDTO judgementUpdateDTO){
        String access_token = get_access_token(token);
        return new ResponseEntity<>(judgementService.update_judgement(access_token, id, judgementUpdateDTO), HttpStatus.OK);
    }



}
