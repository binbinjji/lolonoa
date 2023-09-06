package com.example.gg.judgement.domain.model;

import com.example.gg.feedback.domain.model.Feedback;
import com.example.gg.member.domain.model.Member;
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

//    @CreationTimestamp //시간 자동 입력
//    private Timestamp postTime;

    private LocalDateTime postTime;

    @OneToMany(mappedBy = "judgement", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    @OrderBy("id asc") // 댓글 정렬
    private List<Feedback> feedbacks;

}
