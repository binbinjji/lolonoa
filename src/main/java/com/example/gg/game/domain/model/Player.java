package com.example.gg.game.domain.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Player {
    @Id
    @Column(name = "player_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "game_id")
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
