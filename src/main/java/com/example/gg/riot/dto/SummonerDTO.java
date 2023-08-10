package com.example.gg.riot.dto;

import com.example.gg.riot.domain.model.Summoner;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SummonerDTO {
    private String accountId;
    private int profileIconId;
    private long revisionDate;
    private String name;
    private String id;
    private String puuid;
    private long summonerLevel;

    public Summoner toEntity(){
        return Summoner.builder()
                .nickname(name)
                .encryptedSummonerId(id)
                .puuid(puuid)
                .summonerLevel(summonerLevel)
                .build();
    }
}
