package com.example.demo.domain.user.entity;

import lombok.Getter;

@Getter
public enum UserSocialEnum {
    NAVER(UserSocialEnum.Authority.NAVER),
    KAKAO(UserSocialEnum.Authority.KAKAO),
    GOOGLE(UserSocialEnum.Authority.GOOGLE);

    private final String authority;

    UserSocialEnum(String authority) {
        this.authority = authority;
    }

    public String getSocial() {
        return this.authority;
    }

    public static class Authority {
        public static final String NAVER = "NAVER_USER";
        public static final String KAKAO = "KAKAO_USER";
        public static final String GOOGLE = "GOOGLE_USER";
    }
}
