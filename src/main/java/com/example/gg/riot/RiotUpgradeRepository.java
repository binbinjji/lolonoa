package com.example.gg.riot;

import com.example.gg.riot.domain.model.Summoner;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
@Transactional
public class RiotUpgradeRepository {

    private final EntityManager em;

    public RiotUpgradeRepository(EntityManager em) {
        this.em = em;
    }

    public Optional<Summoner> findByNickname(String nickname){
        Optional<Summoner> summoner = em.createQuery("SELECT s FROM Summoner s WHERE replace(s.nickname, ' ', '')=:nickname", Summoner.class)
                .setParameter("nickname", nickname)
                .getResultList().stream().findAny();

        return summoner;
    }
}
