package com.example.gg.riot.dto;

import lombok.*;

@Getter @Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TierResponseDTO {

    private String tier;
    private String rank;
    private Long wins;
    private Long losses;
    private Long leaguePoints;
}
