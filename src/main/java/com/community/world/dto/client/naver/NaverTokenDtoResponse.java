package com.community.world.dto.client.naver;


import com.community.world.dto.jwt.TokenDtoImpl;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class NaverTokenDtoResponse implements TokenDtoImpl {
    @JsonProperty("access_token")
    private String accessToken;
    @JsonProperty("refresh_token")
    private String refreshToken;
    @JsonProperty("token_type")
    private String tokenType;
    @JsonProperty("expires_in")
    private Integer expiresIn;
    @JsonProperty("error")
    private String error;
    @JsonProperty("error_description")
    private String errorDescription;

}
