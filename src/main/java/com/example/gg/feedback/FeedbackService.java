package com.example.gg.feedback;

import com.example.gg.error.CustomException;
import com.example.gg.error.ErrorCode;
import com.example.gg.feedback.domain.model.Feedback;
import com.example.gg.feedback.dto.FeedbackAddDTO;
import com.example.gg.member.domain.model.Member;
import com.example.gg.member.repository.MemberRepository;
import com.example.gg.security.JwtProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.webjars.NotFoundException;

import java.time.LocalDateTime;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class FeedbackService {

    private final FeedbackRepository feedbackRepository;
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
     * 피드백 리스트 불러오기
     */
    public Page<Feedback> feedbacks_list(Pageable pageable, Long judgement_id){
        return feedbackRepository.findAll(pageable, judgement_id);
    }

    /**
     * 피드백 남기기
     */
    public Feedback add_feedback(String access_token, FeedbackAddDTO feedbackAddDTO){
        Feedback feedback = feedbackRepository.save(
                Feedback.builder()
                        .member(find_member(access_token))
                        .judgement(feedbackAddDTO.getJudgement())
                        .comment(feedbackAddDTO.getComment())
                        .vote(feedbackAddDTO.getVote())
                        .postTime(LocalDateTime.now())
                        .build()
        );
        return feedback;
    }

    /**
     * 피드백 삭제하기
     * 피드백의 멤버와 엑세스 토큰의 멤버가 동일해야 삭제한다.
     */
    public void delete_feedback(String access_token, Long id){
        Member member = find_member(access_token);
        Feedback feedback = feedbackRepository.findById(id).orElseThrow(() ->
                     new NotFoundException("존재하지않는 피드백입니다.")
                );
        if(feedback.getMember().equals(member)){
            feedbackRepository.deleteById(id);
        } else {
            throw new CustomException(ErrorCode.NOT_MY_REQUEST);
        }

    }
}
