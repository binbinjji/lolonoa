package com.example.gg.judgement;

import com.example.gg.judgement.domain.model.Judgement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface JudgementRepository extends JpaRepository<Judgement, Long> {

//    @Modifying
//    @Transactional
//    @Query("update Judgement j set j.outline =:outline, j.myOp=:myOp, j.yourOp=:yourOp where j.id =:id")
//    void updateJudgement(Long id, String outline, String myOp, String yourOp);
}
