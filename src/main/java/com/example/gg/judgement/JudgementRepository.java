package com.example.gg.judgement;

import com.example.gg.feedback.domain.model.Feedback;
import com.example.gg.judgement.domain.model.Judgement;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public interface JudgementRepository extends JpaRepository<Judgement, Long> {

    @Query("select j from Judgement j order by j.id desc")
    Page<Judgement> findAll(Pageable pageable);
}
