package com.example.gg.game;

import com.example.gg.game.domain.model.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PlayerRepository extends JpaRepository<Player, Long> {

    @Query("select p from Player p where p.game.id=:game_id")
    List<Player> findByGame_id(Long game_id);

}
