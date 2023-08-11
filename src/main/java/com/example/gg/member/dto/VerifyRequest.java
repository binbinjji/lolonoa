package com.example.gg.member.dto;

import lombok.Getter;

@Getter
public class VerifyRequest {

    private String email;
    private Integer code;
}
