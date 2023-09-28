package com.example.gg.judgement;

import com.example.gg.error.CustomException;
import com.example.gg.error.ErrorCode;
import com.example.gg.judgement.domain.model.Judgement;
import com.example.gg.judgement.dto.JudgementAddDTO;
import com.example.gg.judgement.dto.JudgementUpdateDTO;
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
public class JudgementService {

    private final JudgementRepository judgementRepository;
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
     * 롤문철 남기기
     */
    public Judgement add_judgement(String access_token, JudgementAddDTO judgementAddDTO){
        Member member = find_member(access_token);

        Judgement savedJudgement = judgementRepository.save(
                Judgement.builder()
                        .member(member)
                        .category(judgementAddDTO.getCategory())
                        .matchId(judgementAddDTO.getMatchId())
                        .myTier(judgementAddDTO.getMyTier())
                        .myRank(judgementAddDTO.getMyRank())
                        .yourTier(judgementAddDTO.getYourTier())
                        .yourRank(judgementAddDTO.getYourRank())
                        .outline(judgementAddDTO.getOutline())
                        .postTime(LocalDateTime.now())
                        .myOp(judgementAddDTO.getMyOp())
                        .yourOp(judgementAddDTO.getYourOp())
                        .build()
        );
        return savedJudgement;
    }


    /**
     * 롤문철 id로 조회
     */
    public Judgement search_judgement(Long id){
        Judgement judgement = judgementRepository.findById(id).orElseThrow(() ->
                new CustomException(ErrorCode.BAD_JUDGEMENT_REQUEST)
        );
        return judgement;
    }

    /**
     * 롤문철 리스트 불러오기
     */
    public Page<Judgement> judgements_all(Pageable pageable){
        return judgementRepository.findAll(pageable);
    }

    /**
     * 롤문철 삭제하기
     */
    public void delete_judgement(String access_token, Long id){
        Member member = find_member(access_token);
        Judgement judgement = judgementRepository.findById(id).orElseThrow(() ->
                        new NotFoundException("해당 롤문철이 존재하지않습니다.")
                );
        if(judgement.getMember().equals(member)){
            judgementRepository.deleteById(id);
        }else{
            throw new CustomException(ErrorCode.BAD_JUDGEMENT_REQUEST);
        }
    }

    /**
     * 롤문철 업데이트
     * 모든 파라미터를 받고 파리미터가 null일 경우, 원래 있던 데이터를 사용한다.
     */
    public Judgement update_judgement(String access_token, Long id, JudgementUpdateDTO judgementUpdateDTO){

        Member member = find_member(access_token);
        Judgement judgement = judgementRepository.findById(id).orElseThrow(() ->
                new NotFoundException("해당 롤문철이 존재하지않습니다.")
        );

        if(judgement.getMember().equals(member)){
            if(judgementUpdateDTO.getOutline() != null){
                judgement.setOutline(judgementUpdateDTO.getOutline());
            }
            if(judgementUpdateDTO.getMyOp() != null){
                judgement.setMyOp(judgementUpdateDTO.getMyOp());
            }
            if(judgementUpdateDTO.getYourOp() != null){
                judgement.setYourOp(judgementUpdateDTO.getYourOp());
            }
            return judgementRepository.save(judgement);
        }
        throw new CustomException(ErrorCode.BAD_JUDGEMENT_REQUEST);
    }

    /**
     * 마이페이지에서 사용
     * 내가 남긴 롤문철들을 확인할 수 있다.
     */
    public Page<Judgement> judgements_by_member(Pageable pageable, String access_token){
        Member member = find_member(access_token);
        return judgementRepository.findJudgementsByMember(pageable, member);
    }


//    public Page<Review> reviews_by_member(Pageable pageable, String access_token){
//        Member member = find_member(access_token);
//        return reviewRepository.findListByMember(pageable, member);
//    }
}
