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
    public void sendEmail(String email){
        Random random = new Random();
        int randomValue = random.nextInt(9000) + 1000;

        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setTo(email);
        simpleMailMessage.setSubject("인증 메일");
        simpleMailMessage.setText("인증번호: " + randomValue);
        javaMailSender.send(simpleMailMessage);
        mailRepository.save(
                Mail.builder()
                        .id(email)
                        .code(randomValue)
                        .expiration(300) // 300 = 60 * 5 (5분)
                        .build()
        );
    }

    public boolean verifyCode(VerifyRequest verifyRequest){
        Mail mail = mailRepository.findById(verifyRequest.getEmail()).get();
        if(mail.getCode().equals(verifyRequest.getCode())){
            return true;
        }
        return false;
    }


}
