package com.example.gg.judgement;

import com.example.gg.judgement.domain.model.Judgement;
import com.example.gg.member.domain.model.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface JudgementRepository extends JpaRepository<Judgement, Long> {

    @Query("select j from Judgement j order by j.id desc")
    Page<Judgement> findAll(Pageable pageable);

    @Query("select j from Judgement j where j.member=:member order by j.id asc")
    Page<Judgement> findJudgementsByMember(Pageable pageable, Member member);

}
