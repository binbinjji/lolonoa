package com.example.gg.feedback;

import com.example.gg.feedback.domain.model.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface FeedbackRepository extends JpaRepository<Feedback, Long> {
}
