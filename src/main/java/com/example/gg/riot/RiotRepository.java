package com.example.gg.riot;

import com.example.gg.member.Member;
import com.example.gg.riot.domain.model.Summoner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RiotRepository extends JpaRepository<Summoner, Long> {
    Optional<Summoner> findByNickname(String nickname);
}
