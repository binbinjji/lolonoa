package com.example.gg.game;

import com.example.gg.game.domain.model.Game;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.Optional;

public interface GameRepository extends JpaRepository<Game, Long> {
    @Override
    Optional<Game> findById(Long id);

    @Query("select g from Game g where g.matchId =:match_id")
    Optional<Game> findByMatch_id(String match_id);
}
