package com.community.world.dto.jwt;

import com.community.world.domain.Member;
import lombok.*;
import org.springframework.http.ResponseEntity;

public class ClientTokenDto {

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Response {

        private String accessToken;
        private Member member;

        public static Response getTokenDto(
                ResponseEntity<? extends TokenDtoImpl> tokenResponse) {
            TokenDtoImpl tokenResponseBody = tokenResponse.getBody();
            return Response.builder()
                    .accessToken(tokenResponseBody.getAccessToken())
                    .build();
        }
    }
}
