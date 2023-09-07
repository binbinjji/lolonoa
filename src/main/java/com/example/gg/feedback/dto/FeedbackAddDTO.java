package com.example.gg.feedback.dto;

import com.example.gg.judgement.domain.model.Judgement;
import lombok.*;

@Getter @Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FeedbackAddDTO {
    private Judgement judgement;
    private String comment;
    private String vote;
}
