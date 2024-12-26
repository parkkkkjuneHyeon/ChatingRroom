package com.community.world.dto.jwt;


import lombok.*;


public class TokenDto {

    @Setter
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Response {
        private Long memberId;
        private String token;

    }
}
