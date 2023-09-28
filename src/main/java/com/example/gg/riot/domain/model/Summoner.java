package com.example.gg.riot.domain.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Summoner {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
//    private String accountId;
//    private int profileIconId;
//    private long revisionDate;
    @Column(unique = true)
    private String nickname;
    private String encryptedSummonerId;
    private String puuid;
    private long summonerLevel;

    public void update(long summonerLevel){
        this.summonerLevel = summonerLevel;
    }


}
