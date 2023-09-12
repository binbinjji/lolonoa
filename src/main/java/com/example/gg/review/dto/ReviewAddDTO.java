package com.example.gg.review.dto;

import lombok.*;

@Getter @Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReviewAddDTO {

    private String matchId;

    private String nickname;

    private String content;

    private Integer score;
}
