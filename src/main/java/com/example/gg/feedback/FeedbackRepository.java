package com.example.gg.feedback;

import com.example.gg.feedback.domain.model.Feedback;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface FeedbackRepository extends JpaRepository<Feedback, Long> {

    @Query("select f from Feedback f left join f.judgement where f.judgement.id =:judgement_id order by f.id asc ")
    Page<Feedback> findAll(Pageable pageable, Long judgement_id);
}
