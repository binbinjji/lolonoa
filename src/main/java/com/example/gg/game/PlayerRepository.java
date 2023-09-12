package com.example.gg.game;

import com.example.gg.game.domain.model.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PlayerRepository extends JpaRepository<Player, Long> {

    //@Query("select g from Game g where g.matchId =:match_id")

    @Query("select p from Player p where p.game.id=:game_id")
    List<Player> findByGame_id(Long game_id);

//    @Query("select p from Player p left join p.game where p.game.matchId=:match_id")
//    List<Player> findAll(Long match_id);
}
