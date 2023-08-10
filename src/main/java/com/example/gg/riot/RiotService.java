package com.example.gg.riot;

import com.example.gg.riot.domain.model.Summoner;
import com.example.gg.riot.dto.SummonerDTO;
import com.example.gg.riot.dto.SummonerResponseDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.io.IOException;
import java.util.Optional;

import org.apache.http.HttpResponse;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class RiotService {

    private String mykey = "RGAPI-ad099291-9ec5-4e69-82b7-0a77dd3a437e";
    private ObjectMapper objectMapper = new ObjectMapper();
    private final RiotRepository riotRepository;

    public Summoner callRiotAPISummonerByName(String summonerName){
        SummonerDTO result;
        String serverUrl = "https://kr.api.riotgames.com";

//        Optional<Summoner> findSummoner = riotRepository.findByNickname(summonerName);
//        if(findSummoner.isPresent()){
//            return findSummoner.get();
//        }

        try {
            CloseableHttpClient client = HttpClientBuilder.create().build();
            HttpGet request = new HttpGet(serverUrl + "/lol/summoner/v4/summoners/by-name/" + summonerName + "?api_key=" + mykey);
            HttpResponse response = (HttpResponse) client.execute(request);
            if(response.getStatusLine().getStatusCode() != 200){
                // 오류
                return null;
            }

            HttpEntity entity = response.getEntity();
            result = objectMapper.readValue(entity.getContent(), SummonerDTO.class);
            Summoner summoner = result.toEntity();
            riotRepository.save(summoner);
//            SummonerResponseDTO summonerResponseDTO = result.toEntity();
            return summoner;
        } catch (IOException e){
            e.printStackTrace();
            return null;
        }

    }





}
