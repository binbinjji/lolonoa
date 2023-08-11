package com.example.gg.member.dto;

import com.example.gg.member.domain.model.Authority;
import com.example.gg.member.domain.model.Member;
import com.example.gg.security.TokenDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SignResponse {

    private Long id;

    private String account;


    private String nickname;

    private List<Authority> roles = new ArrayList<>();

    private TokenDto token;

    public SignResponse(Member member) {
        this.id = member.getId();
        this.account = member.getAccount();
        this.nickname = member.getNickname();
        this.roles = member.getRoles();
    }
}
