package com.example.gg.review.domain.model;

import com.example.gg.feedback.domain.model.Feedback;
import com.example.gg.member.domain.model.Member;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nickname;

    private String content;

    private Integer score;

    @JoinColumn(name = "member_id")
    @ManyToOne(optional = false)
    private Member member;


}
