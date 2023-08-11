package com.example.gg.member.service;

import com.example.gg.member.domain.model.Mail;
import com.example.gg.member.dto.VerifyRequest;
import com.example.gg.member.repository.MailRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import java.util.Random;

@Service
@RequiredArgsConstructor
@Slf4j
public class MailService {
    private final JavaMailSender javaMailSender;
    private final MailRepository mailRepository;


    /*한번 더 인증코드를 보내는 경우, redis에서 자동으로 인증코드가 갱신된다.*/
    public boolean sendEmail(String email){
        Random random = new Random();
        int randomValue = random.nextInt(9000) + 1000;

        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setTo(email);
        simpleMailMessage.setSubject("인증 메일");
        simpleMailMessage.setText("인증번호: " + randomValue);
        try{
            javaMailSender.send(simpleMailMessage);
            Mail mail = mailRepository.save(
                    Mail.builder()
                            .id(email)
                            .code(randomValue)
                            .expiration(300) // 300 = 60 * 5 (5분)
                            .build()
            );
            return true;
        } catch (Exception e){
            return false;
        }
    }

    public boolean verifyCode(VerifyRequest verifyRequest) throws Exception {
//        Mail mail = mailRepository.findById(verifyRequest.getEmail())
//                .orElseThrow(() -> new Exception("잘못된 접근입니다.(인증번호 요청필요)"));

        Mail mail = mailRepository.findById(verifyRequest.getEmail()).get();
        if(mail.getCode().equals(verifyRequest.getCode())){
            return true;
        }
        return false;
    }


}
