package com.example.gg.member.domain.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Id;
import lombok.*;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

import java.util.concurrent.TimeUnit;

@Getter @Setter
@RedisHash("mail")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Mail {

    @Id
    @JsonIgnore
    private String id;

    private Integer code;

    @TimeToLive(unit= TimeUnit.SECONDS)
    private Integer expiration;

//    public void setCode(String code){
//        this.code = code;
//    }
//
//    public void setExpiration(Integer expiration){
//        this.expiration = expiration;
//    }
}
