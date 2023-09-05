package com.example.gg.judgement.dto;

import lombok.*;

@Getter @Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class JudgementAddDTO {
    private String category;
    private String matchId;
    private String myTier;
    private String myRank;
    private String yourTier;
    private String yourRank;
    private String outline;
    private String myOp;
    private String yourOp;

}
