package com.example.gg.riot;

import com.example.gg.error.CustomException;
import com.example.gg.error.ErrorCode;
import com.example.gg.game.GameRepository;
import com.example.gg.game.PlayerRepository;
import com.example.gg.game.domain.model.Game;
import com.example.gg.game.domain.model.Player;
import com.example.gg.riot.domain.model.Summoner;
import com.example.gg.riot.dto.SummonerDTO;
import com.example.gg.riot.dto.TierResponseDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.apache.http.HttpResponse;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class RiotService {

    @Value("${riot.key}")
    private String mykey;
    private ObjectMapper objectMapper = new ObjectMapper();
    private final RiotRepository riotRepository;
    private final RiotUpgradeRepository riotUpgradeRepository;
    private final GameRepository gameRepository;
    private final PlayerRepository playerRepository;


    /**
     * 전적관련 정보(티어, 랭크, wins, losses, leaguePoints) 받아오기
     */
    public TierResponseDTO getTier(String encryptedSummonerId){

        try{
            CloseableHttpClient client = HttpClientBuilder.create().build();
            HttpGet request = new HttpGet( "https://kr.api.riotgames.com/lol/league/v4/entries/by-summoner/" + encryptedSummonerId + "?api_key=" + mykey);
            HttpResponse response = (HttpResponse) client.execute(request);
            if(response.getStatusLine().getStatusCode() != 200){
                // 오류
                return null;
            }
            HttpEntity entity = response.getEntity();
            String matchIds = EntityUtils.toString(entity);
            JSONParser parser = new JSONParser();
            JSONArray arr = (JSONArray) parser.parse(matchIds);

            TierResponseDTO result = new TierResponseDTO();
            for (int i = 0; i<arr.size(); i++) {
                JSONObject obj =  (JSONObject) arr.get(i);
                if(obj.get("queueType").equals("RANKED_SOLO_5x5")){
                    result.setTier((String) obj.get("tier"));
                    result.setRank((String) obj.get("rank"));
                    result.setWins((Long) obj.get("wins"));
                    result.setLosses((Long) obj.get("losses"));
                    result.setLeaguePoints((Long) obj.get("leaguePoints"));
                    return result;
                }
            }
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
        throw new CustomException(ErrorCode.BAD_RIOT_REQUEST);
    }

    /**
     * 1. summonerName(=nickname)으로 DB에서 가져온다.
     * 2. 없을 시 Riot api 요청으로 프로필 정보를 불러온다.
     */
    public Summoner callRiotAPISummonerByName(String nickname){

        String replacedNickname = nickname.replace(" ","");
        Optional<Summoner> find_summoner = riotUpgradeRepository.findByNickname(replacedNickname);
        if(find_summoner.isPresent()){
            return find_summoner.get();
        }

        nickname = nickname.replaceAll(" ","%20");
        SummonerDTO result;

        try {
            CloseableHttpClient client = HttpClientBuilder.create().build();
            HttpGet request = new HttpGet( "https://kr.api.riotgames.com/lol/summoner/v4/summoners/by-name/" + nickname + "?api_key=" + mykey);
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
            System.out.println(e.getMessage());
        }
        throw new CustomException(ErrorCode.BAD_RIOT_REQUEST);
    }

    /**
     * puuid로 macthId 5개를 불러온다.
     */
    public String[] getMatchInfo(String puuid){
        try {
            CloseableHttpClient client = HttpClientBuilder.create().build();
            HttpGet request = new HttpGet("https://asia.api.riotgames.com/lol/match/v5/matches/by-puuid/" + puuid +"/ids?type=ranked&start=0&count=5&api_key=" + mykey);
            HttpResponse response = (HttpResponse) client.execute(request);
            if(response.getStatusLine().getStatusCode() != 200){
                // 오류
                return null;
            }
            HttpEntity entity = response.getEntity();
            String matchIds = EntityUtils.toString(entity);

            String substring = matchIds.substring(2, matchIds.length() - 2); // 양 옆의 [", "] 없애기
            System.out.println(substring);
            String[] matchIdArray = substring.split("\",\"");
            return matchIdArray;

        } catch (IOException e){
            System.out.println(e.getMessage());
        }
        throw new CustomException(ErrorCode.BAD_RIOT_REQUEST);
    }

    /**
     * matchId로 해당 경기 정보를 불러온다.
     */
    public JSONObject matchDetailInfo(String matchId) {

        JSONObject jsonObject = new JSONObject();
        JSONArray jsonArray = new JSONArray();

        Optional<Game> findGame = gameRepository.findByMatch_id(matchId);
        if(findGame.isPresent()){
            Game game = findGame.get();
            jsonObject.put("gameMode", game.getMode());
            jsonObject.put("gameDuration", game.getDuration());
            List<Player> players = playerRepository.findByGame_id(game.getId());
            log.info("이미 db에 존재하는 플레이어 = " + players.get(0).getChampionName());
            for (Player player : players) {
                JSONObject data = new JSONObject();
                data.put("summonerName", player.getSummonerName());
                data.put("championName", player.getChampionName());
                data.put("individualPosition", player.getIndividualPosition());
                data.put("champLevel", player.getChampLevel());
                data.put("kills", player.getKills());
                data.put("deaths", player.getDeaths());
                data.put("assists", player.getAssists());
                data.put("totalDamageDealtToChampions", player.getTotalDamageDealtToChampions());
                data.put("totalDamageTaken", player.getTotalDamageTaken());
                data.put("goldEarned", player.getGoldEarned());
                data.put("item0", player.getItem0());
                data.put("item1", player.getItem1());
                data.put("item2", player.getItem2());
                data.put("item3", player.getItem3());
                data.put("item4", player.getItem4());
                data.put("item5", player.getItem5());
                data.put("item6", player.getItem6());
                data.put("wardsPlaced", player.getWardsPlaced());
                data.put("wardsKilled", player.getWardsKilled());
                jsonArray.add(data);
            }
            jsonObject.put("users", jsonArray);
            return jsonObject;
        }

        try{
            CloseableHttpClient client = HttpClientBuilder.create().build();
            HttpGet request = new HttpGet("https://asia.api.riotgames.com/lol/match/v5/matches/" + matchId + "?api_key=" + mykey);
            HttpResponse response = (HttpResponse) client.execute(request);
            if(response.getStatusLine().getStatusCode() != 200){
                // 오류
                return null;
            }
            HttpEntity entity = response.getEntity();
            String matchInfo = EntityUtils.toString(entity);

            JSONParser parser = new JSONParser();
            Object obj = parser.parse(matchInfo);
            JSONObject jsonObj = (JSONObject) obj;

            JSONObject info = (JSONObject) jsonObj.get("info");
            JSONArray participants = (JSONArray) info.get("participants");

            String gameMode = (String) info.get("gameMode");
            jsonObject.put("gameMode", gameMode);

            Long gameDuration = (Long) info.get("gameDuration");
            jsonObject.put("gameDuration", gameDuration);

            Game savedGame = gameRepository.save(
                    Game.builder()
                            .matchId(matchId)
                            .mode(gameMode)
                            .duration(gameDuration)
                            .build()
            );
            for (int i = 0; i < participants.size(); i++) {
                JSONObject participant = (JSONObject) participants.get(i);
                JSONObject data = new JSONObject();
                String summonerName = (String) participant.get("summonerName");
                data.put("summonerName", summonerName);

                String championName = (String) participant.get("championName");
                data.put("championName", championName);

                String individualPosition = (String) participant.get("individualPosition");
                data.put("individualPosition", individualPosition);

                Long champLevel = (Long) participant.get("champLevel");
                data.put("champLevel", champLevel);

                Long kills = (Long) participant.get("kills");
                data.put("kills", kills);

                Long deaths = (Long) participant.get("deaths");
                data.put("deaths", deaths);

                Long assists = (Long) participant.get("assists");
                data.put("assists", assists);

                Long totalDamageDealtToChampions = (Long) participant.get("totalDamageDealtToChampions");
                data.put("totalDamageDealtToChampions", totalDamageDealtToChampions);

                Long totalDamageTaken = (Long) participant.get("totalDamageTaken");
                data.put("totalDamageTaken", totalDamageTaken);

                Long goldEarned = (Long) participant.get("goldEarned");
                data.put("goldEarned", goldEarned);

                Long item0 = (Long) participant.get("item0");
                data.put("item0", item0);

                Long item1 = (Long) participant.get("item1");
                data.put("item1", item1);

                Long item2 = (Long) participant.get("item2");
                data.put("item2", item2);

                Long item3 = (Long) participant.get("item3");
                data.put("item3", item3);

                Long item4 = (Long) participant.get("item4");
                data.put("item4", item4);

                Long item5 = (Long) participant.get("item5");
                data.put("item5", item5);

                Long item6 = (Long) participant.get("item6");
                data.put("item6", item6);

                Long wardsPlaced = (Long) participant.get("wardsPlaced");
                data.put("wardsPlaced", wardsPlaced);

                Long wardsKilled = (Long) participant.get("wardsKilled");
                data.put("wardsKilled", wardsKilled);

                Player player = playerRepository.save(
                        Player.builder()
                                .game(savedGame)
                                .summonerName(summonerName)
                                .championName(championName)
                                .individualPosition(individualPosition)
                                .champLevel(champLevel)
                                .kills(kills)
                                .deaths(deaths)
                                .assists(assists)
                                .totalDamageDealtToChampions(totalDamageDealtToChampions)
                                .totalDamageTaken(totalDamageTaken)
                                .goldEarned(goldEarned)
                                .item0(item0)
                                .item1(item1)
                                .item2(item2)
                                .item3(item3)
                                .item4(item4)
                                .item5(item5)
                                .item6(item6)
                                .wardsPlaced(wardsPlaced)
                                .wardsKilled(wardsKilled)
                                .build()
                );
                jsonArray.add(data);
            }
            jsonObject.put("users", jsonArray);
            return jsonObject;
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        throw new CustomException(ErrorCode.BAD_RIOT_REQUEST);
    }

    /**
     * 프로필 정보를 업데이트 한다.
     */
    public Summoner update_summoner(String nickname){

        String replacedNickname = nickname.replace(" ","");
        nickname = nickname.replaceAll(" ","%20");

        try {
            CloseableHttpClient client = HttpClientBuilder.create().build();
            HttpGet request = new HttpGet( "https://kr.api.riotgames.com/lol/summoner/v4/summoners/by-name/" + nickname + "?api_key=" + mykey);
            HttpResponse response = (HttpResponse) client.execute(request);
            if(response.getStatusLine().getStatusCode() != 200){
                // 오류
                return null;
            }
            SummonerDTO result;
            HttpEntity entity = response.getEntity();
            result = objectMapper.readValue(entity.getContent(), SummonerDTO.class);
            Summoner summoner = result.toEntity();

            Optional<Summoner> find_summoner = riotUpgradeRepository.findByNickname(replacedNickname);
            if(find_summoner.isPresent()){
                find_summoner.get().update(summoner.getSummonerLevel());
            }
            return find_summoner.get();
        } catch (IOException e){
            System.out.println(e.getMessage());
        }
        throw new CustomException(ErrorCode.BAD_RIOT_REQUEST);
    }
}
