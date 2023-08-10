package com.example.gg.riot;

import com.example.gg.riot.domain.model.Summoner;
import com.example.gg.riot.dto.SummonerDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@ResponseBody
public class RiotController {
    private final RiotService riotService;

    @PostMapping(value = "/summonerByName")
    public Summoner callSummonerByName(String summonerName){

        summonerName = summonerName.replaceAll(" ","%20");

//        SummonerDTO apiResult = riotService.callRiotAPISummonerByName(summonerName);
//        return apiResult;
        Summoner apiResult = riotService.callRiotAPISummonerByName(summonerName);
        return apiResult;
    }
}
