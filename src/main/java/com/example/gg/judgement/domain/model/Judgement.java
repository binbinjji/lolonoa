package com.example.gg.judgement.domain.model;

import com.example.gg.feedback.domain.model.Feedback;
import com.example.gg.member.domain.model.Member;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Judgement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "member_id")
    @ManyToOne(optional = false)
    private Member member;

    private String category;

    private String matchId;

    private String myTier;
    private String myRank;

    private String yourTier;
    private String yourRank;

    private String outline;

    private String myOp;

    private String yourOp;

    private LocalDateTime postTime;

    @JsonIgnore
    @OneToMany(mappedBy = "judgement", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    private List<Feedback> feedbacks;

}
