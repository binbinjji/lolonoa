package com.example.gg.review.dto;

import com.example.gg.game.domain.model.Game;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReviewResponseDTO {

    private Long id;
    private Game game;
    private String summonerName;
    private String championName;
    private String individualPosition;
    private Long champLevel;
    private Long kills;
    private Long deaths;
    private Long assists;
    private Long totalDamageDealtToChampions;
    private Long totalDamageTaken;
    private Long goldEarned;
    private Long item0;
    private Long item1;
    private Long item2;
    private Long item3;
    private Long item4;
    private Long item5;
    private Long item6;
    private Long wardsPlaced;
    private Long wardsKilled;
}
