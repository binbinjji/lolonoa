package com.example.gg.feedback.domain.model;

import com.example.gg.judgement.domain.model.Judgement;
import com.example.gg.member.domain.model.Member;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class Feedback {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "judgement_id")
    private Judgement judgement;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    private String comment;

    private Long vote;

    private LocalDateTime postTime;
}
