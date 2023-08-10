package com.example.gg.riot.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SummonerResponseDTO {
    private String nickname;
    private String encryptedSummonerId;
    private String puuid;
    private long summonerLevel;

//    public SummonerResponseDTO(String name, String encryptedSummonerId, String puuid, Long summonerLevel){
//        this.nickname = name;
//        this.encryptedSummonerId = encryptedSummonerId;
//        this.puuid = puuid;
//        this.summonerLevel = summonerLevel;
//    }
}
