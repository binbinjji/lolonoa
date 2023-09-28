package com.example.gg.riot;

import com.example.gg.riot.domain.model.Summoner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface RiotRepository extends JpaRepository<Summoner, Long> {
}
