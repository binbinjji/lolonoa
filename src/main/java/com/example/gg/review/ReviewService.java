package com.example.gg.review;

import com.example.gg.error.CustomException;
import com.example.gg.error.ErrorCode;

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
import org.webjars.NotFoundException;

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

    public Member find_member(String access_token){
        String account = jwtProvider.getAccount(access_token);
        Member member = memberRepository.findByAccount(account).orElseThrow(() ->
                new CustomException(ErrorCode.NO_EXIST_MEMBER)
        );
        return member;
    }

    /**
     * 멤버의 리스트를 불러온다. 양이 적을 것 같아서 페이지가아닌 리스트로 했다.
     */
    public List<Review> reviews_list(String nickname){
        return reviewRepository.findByNickname(nickname);
    }

    /**
     * 1. 액세스 토큰의 멤버를 가져온다.
     * 2. 해당 멤버의 모스트 닉네임을 추출한다.
     * 3. 모스트와 DTO의 닉네임, macthID로 리뷰를 DB에서 찾아본다.
     * 4. 이미 있다면 에러. 없다면 저장.
     */
    public Review add_review(String access_token, ReviewAddDTO reviewAddDTO){
        Member member = find_member(access_token);
        String most = member.getMost().getNick();
        Optional<Review> review = reviewRepository.findByConditions(most, reviewAddDTO.getNickname(), reviewAddDTO.getMatchId());
        if(review.isPresent()){
            throw new CustomException(ErrorCode.BAD_REVIEW_REQUEST);
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

    /**
     * 리뷰 삭제
     */
    public void delete_review(String access_token, Long id){
        Member member = find_member(access_token);
        Review review = reviewRepository.findById(id).orElseThrow(() ->
                new NotFoundException("해당 리뷰는 존재하지않습니다.")
        );
        if(review.getMember().equals(member)){
            reviewRepository.deleteById(id);
        } else {
            throw new CustomException(ErrorCode.NOT_MY_REQUEST);
        }
    }

    /**
     * 마이페이지에서 사용
     * 내가 남긴 리뷰들을 확인할 수 있다.
     */
    public Page<Review> reviews_by_member(Pageable pageable, String access_token){
        Member member = find_member(access_token);
        return reviewRepository.findReviewsByMember(pageable, member);
    }

}
