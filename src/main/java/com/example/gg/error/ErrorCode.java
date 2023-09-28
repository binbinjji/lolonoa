package com.example.gg.error;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    /* 400 BAD_REQUEST : 잘못된 요청 */

    BAD_RIOT_REQUEST(BAD_REQUEST, "[Riot] API 요청 실패"),
    BAD_REVIEW_REQUEST(BAD_REQUEST, "[Review] 이미 리뷰를 남겼습니다."),
    NO_EXIST_MEMBER(BAD_REQUEST, "존재하지않는 유저입니다."),
    BAD_JUDGEMENT_REQUEST(BAD_REQUEST, "[Judgement] 잘못된 요청입니다."),
    NOT_MY_REQUEST(BAD_REQUEST, "잘못된 접근입니다.");
//    USER_ID_NOT_THE_SAME(BAD_REQUEST, "로그인 정보[ID]가 올바르지 않습니다."),
//    SOCIAL_LOGIN_ID_AND_AUTH_PROVIDER_NOT_THE_SAME(BAD_REQUEST, "로그인 정보[SOCIAL_LOGIN_ID, AUTH_PROVIDER]가 올바르지 않습니다."),
//    REFRESH_TOKEN_NOT_FOUND(BAD_REQUEST, "쿠키에 리프레시 토큰이 존재하지 않습니다.");

    private final HttpStatus httpStatus;
    private final String detail;

}