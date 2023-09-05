package com.example.gg.member.dto;

import com.example.gg.member.domain.model.Authority;
import com.example.gg.member.domain.model.Member;
import com.example.gg.security.TokenDto;
import lombok.*;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SignResponse {

    private Long id;

    private String account;

    private String nickname;

    private List<Authority> roles;

//    private List<GameName> gameNames;

    private TokenDto token;


    public SignResponse(Member member) {
        this.id = member.getId();
        this.account = member.getAccount();
        this.nickname = member.getNickname();
        this.roles = member.getRoles();
//        this.gameNames = member.getGameNames();
    }
}
