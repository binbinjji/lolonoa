package com.example.gg.riot;

import com.example.gg.riot.domain.model.Summoner;
import com.example.gg.riot.dto.TierResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name="라이엇", description = "롤 api key를 갱신해주지 않으면 에러가 뜨는 점 유의. ")
@RestController
@RequiredArgsConstructor
@RequestMapping("/riot")
public class RiotController {
    private final RiotService riotService;

    @Operation(summary = "닉네임으로 정보 받기", description = "id(랜덤), nickname, encryptedSummonerId, puuid, summonerLevel 추출, 필요하다면 profileIconId 추가 가능")
    @PostMapping("/summonerByName")
    public ResponseEntity<Summoner> callSummonerByName(@RequestParam String summonerName){
        return new ResponseEntity<>(riotService.callRiotAPISummonerByName(summonerName), HttpStatus.OK);
    }

    @Operation(summary = "업데이트", description = "레벨만 업데이트됨")
    @PostMapping("/update")
    public ResponseEntity<Summoner> update_summoner(@RequestParam String summonerName){
        return new ResponseEntity<>(riotService.update_summoner(summonerName),HttpStatus.OK);
    }

    @Operation(summary = "puuid로 경기id 받기", description = "최근 경기 5판의 id(KR_~~)값을 얻음")
    @PostMapping("/match")
    public ResponseEntity<JSONArray> getMatchInfo(@RequestParam String puuid){
        return new ResponseEntity<>(riotService.getMatchInfo(puuid), HttpStatus.OK);
    }


    @Operation(summary = "경기 id로 경기 전적 받기", description = "gameMode, gameDuration, users(summonerName, championName, champLevel, individualPosition, kills, deaths, assists, totalDamageDealtToChampions(가한 피해량), totalDamageTaken(받은 피해량), goldEarned(골드량), item0~6, wardsPlaced, wardsKilled)")
    @PostMapping("/game")
    public ResponseEntity<JSONObject> getGameInfo(@RequestParam String matchId) {
        return new ResponseEntity<>(riotService.matchDetailInfo(matchId), HttpStatus.OK);
    }

    @Operation(summary = "전적관련 정보 받기", description = "tier, rank, wins, losses, leaguePoints")
    @PostMapping("/tier")
    public ResponseEntity<TierResponseDTO> getGameTier(String encryptedSummonerId){
        return new ResponseEntity<>(riotService.getTier(encryptedSummonerId), HttpStatus.OK);
    }


}
