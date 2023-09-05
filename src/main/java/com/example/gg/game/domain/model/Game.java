package com.example.gg.game.domain.model;

import com.example.gg.member.domain.model.Member;
import com.example.gg.member.domain.model.Nicks;
import com.example.gg.riot.domain.model.Summoner;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Game {

    @Id
    @Column(name = "game_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String mode;

    private String matchId;

    private Long duration;

    @OneToMany(mappedBy = "game", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Player> players = new ArrayList<>();

//    @OneToMany(mappedBy = "game", cascade = CascadeType.ALL, orphanRemoval = true)
//    @Builder.Default
//    List<Player> red = new ArrayList<>();
//
//    @OneToMany(mappedBy = "game", cascade = CascadeType.ALL, orphanRemoval = true)
//    @Builder.Default
//    List<Player> blue = new ArrayList<>();
//
//    public void add_red(Player player){
//        red.add(player);
//    }
//    public void add_blue(Player player){
//        blue.add(player);
//    }

}
