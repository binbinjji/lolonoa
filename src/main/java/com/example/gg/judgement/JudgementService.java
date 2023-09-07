package com.example.gg.judgement;

import com.example.gg.judgement.domain.model.Judgement;
import com.example.gg.judgement.dto.JudgementAddDTO;
import com.example.gg.judgement.dto.JudgementUpdateDTO;
import com.example.gg.member.domain.model.Member;
import com.example.gg.member.repository.MemberRepository;
import com.example.gg.security.JwtProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
                new IllegalArgumentException("해당 회원이 존재하지않습니다.")
        );
        return member;
    }
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

    public Judgement search_judgement(Long id){
        Judgement judgement = judgementRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException("존재하지않는 롤문철입니다.")
        );
        return judgement;
    }

    public void delete_judgement(String access_token, Long id){
        Member member = find_member(access_token);
        Judgement judgement = judgementRepository.findById(id).orElseThrow(() ->
                        new IllegalArgumentException("해당 롤문철이 존재하지않습니다.")
                );
        if(judgement.getMember().equals(member)){
            judgementRepository.deleteById(id);
        }else{
            throw new IllegalArgumentException("잘못된 요청입니다(본인의 게시물이 아닙니다)");
        }
    }

    public Judgement update_judgement(String access_token, Long id, JudgementUpdateDTO judgementUpdateDTO){

        Member member = find_member(access_token);
        Judgement judgement = judgementRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException("해당 회원이 존재하지않습니다.")
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
        throw new IllegalArgumentException("잘못된 요청입니다(본인의 게시물이 아닙니다)");
    }
}
