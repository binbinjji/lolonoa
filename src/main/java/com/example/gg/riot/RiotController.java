package com.example.gg.riot;

import com.example.gg.riot.domain.model.Summoner;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@ResponseBody
public class RiotController {
    private final RiotService riotService;

    @PostMapping(value = "/riot/summonerByName")
    public Summoner callSummonerByName(String summonerName){

        summonerName = summonerName.replaceAll(" ","%20");
        Summoner apiResult = riotService.callRiotAPISummonerByName(summonerName);
        return apiResult;
    }
}
