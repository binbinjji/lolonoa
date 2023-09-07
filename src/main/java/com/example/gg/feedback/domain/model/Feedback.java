package com.example.gg.feedback.domain.model;

import com.example.gg.judgement.domain.model.Judgement;
import com.example.gg.member.domain.model.Member;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
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

//    @Enumerated(EnumType.STRING)
    private String vote;

    private LocalDateTime postTime;
}
