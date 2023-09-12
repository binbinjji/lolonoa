package com.example.gg.review;

import com.example.gg.game.GameRepository;
import com.example.gg.game.PlayerRepository;
import com.example.gg.game.domain.model.Game;
import com.example.gg.game.domain.model.Player;
import com.example.gg.member.domain.model.Member;
import com.example.gg.member.repository.MemberRepository;
import com.example.gg.review.domain.model.Review;
import com.example.gg.review.dto.ReviewAddDTO;
import com.example.gg.security.JwtProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class ReviewService {

    private final ReviewRepository reviewRepository;

    private final JwtProvider jwtProvider;
    private final MemberRepository memberRepository;
    private final GameRepository gameRepository;
    private final PlayerRepository playerRepository;

    public Member find_member(String access_token){
        String account = jwtProvider.getAccount(access_token);
        Member member = memberRepository.findByAccount(account).orElseThrow(() ->
                new IllegalArgumentException("해당 회원이 존재하지않습니다.")
        );
        return member;
    }

    public List<Review> reviews_list(String nickname){
        return reviewRepository.findByNickname(nickname);
    }

    public Review add_review(String access_token, ReviewAddDTO reviewAddDTO){
        Member member = find_member(access_token);
        String most = member.getMost().getNick();
        Optional<Review> review = reviewRepository.findByConditions(most, reviewAddDTO.getNickname(), reviewAddDTO.getMatchId());
        if(review.isPresent()){
            log.info("Match Id=" +review.get().getMatchId());
            throw new IllegalArgumentException("이미 리뷰를 남겼습니다.");
        }

        return reviewRepository.save(
                Review.builder()
                        .matchId(reviewAddDTO.getMatchId())
                        .sender(most)
                        .nickname(reviewAddDTO.getNickname())
                        .content(reviewAddDTO.getContent())
                        .score(reviewAddDTO.getScore())
                        .member(member)
                        .build()
        );
    }

//    public Review add_review(String access_token, ReviewAddDTO reviewAddDTO){
//        Member member = find_member(access_token);
//        String most = member.getMost().getNick();
//        Optional<Review> review = reviewRepository.findByConditions(most, reviewAddDTO.getNickname(), reviewAddDTO.getMatchId());
//        if(review.isPresent()){
//            throw new IllegalArgumentException("잘못된 요청입니다");
//        }
//
//        Game game = gameRepository.findByMatch_id(reviewAddDTO.getMatchId()).orElseThrow(() ->
//                        new IllegalArgumentException("잘못된 접근입니다.")
//                );
//
//        List<String> list = list(game.getId());
//
//
//
//        boolean a = list.contains(most);
//        boolean b = list.contains(reviewAddDTO.getNickname());
//
//        if(a && b){
//            return reviewRepository.save(
//                    Review.builder()
//                            .matchId(reviewAddDTO.getMatchId())
//                            .sender(most)
//                            .nickname(reviewAddDTO.getNickname())
//                            .content(reviewAddDTO.getContent())
//                            .score(reviewAddDTO.getScore())
//                            .member(member)
//                            .build()
//            );
//        }
//        throw new IllegalArgumentException("잘못된 요청입니다");
//    }

    public List<String> list(Long game_id){
        List<Player> players = playerRepository.findByGame_id(game_id);
        List<String> player_array = new ArrayList<>();
        for (Player player : players) {
            player_array.add(player.getSummonerName());
        }

        return player_array;
    }

    public void delete_review(String access_token, Long id){
        Member member = find_member(access_token);
        Review review = reviewRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException("해당 리뷰는 존재하지않습니다.")
        );
        if(review.getMember().equals(member)){
            reviewRepository.deleteById(id);
        } else {
            throw new IllegalArgumentException("잘못된 요청입니다(본인의 리뷰가 아닙니다)");
        }
    }
}
