package com.example.gg.game;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class GameController {
    private final GameService gameService;

    @PostMapping("/game/add")
    public void add_game(){

    }
}
