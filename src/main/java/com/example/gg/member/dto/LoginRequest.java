package com.example.gg.member.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequest {

    private String account;

    private String password;
}
