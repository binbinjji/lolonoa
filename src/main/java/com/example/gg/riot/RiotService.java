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

    private String mykey = "RGAPI-2accff20-203f-4928-a908-242e98a4b91d";
    private ObjectMapper objectMapper = new ObjectMapper();
    private final RiotRepository riotRepository;

    public Summoner callRiotAPISummonerByName(String nickname){

        Optional<Summoner> find_summoner = riotRepository.findByNickname(nickname);
        if(find_summoner.isPresent()){
            //log.info("이미 db에 존재");
            return find_summoner.get();
        }

        SummonerDTO result;
        String serverUrl = "https://kr.api.riotgames.com";

        try {
            CloseableHttpClient client = HttpClientBuilder.create().build();
            HttpGet request = new HttpGet(serverUrl + "/lol/summoner/v4/summoners/by-name/" + nickname + "?api_key=" + mykey);
            HttpResponse response = (HttpResponse) client.execute(request);
            if(response.getStatusLine().getStatusCode() != 200){
                // 오류
                return null;
            }

            HttpEntity entity = response.getEntity();
            result = objectMapper.readValue(entity.getContent(), SummonerDTO.class);
            Summoner summoner = result.toEntity();
            riotRepository.save(summoner);
            return summoner;
        } catch (IOException e){
            e.printStackTrace();
            return null;
        }

    }





}
