package com.example.gg;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class Controller {

    @GetMapping("/healthcheck")
    public ResponseEntity<Integer> health_check(){
        return ResponseEntity.ok().body(200);
    }
}
