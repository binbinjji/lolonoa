package com.example.gg.judgement.domain.model;

import com.example.gg.judgement.dto.JudgementUpdateDTO;
import com.example.gg.member.domain.model.Member;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.util.ObjectUtils;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Entity
//@DynamicUpdate
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


}
