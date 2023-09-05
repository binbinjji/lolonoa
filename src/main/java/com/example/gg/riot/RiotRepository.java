package com.example.gg.riot;

import com.example.gg.riot.domain.model.Summoner;
import jakarta.persistence.EntityManager;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
@Transactional
public interface RiotRepository extends JpaRepository<Summoner, Long> {

//    @Query("select s from Summoner s where replace(s.nickname,' ', '')=:nickname)
    Optional<Summoner> findByNickname(String nickname);


}
