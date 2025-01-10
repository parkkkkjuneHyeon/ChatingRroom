package com.community.world.client.google;


import com.community.world.client.ClientList;
import com.community.world.client.OauthClient;
import com.community.world.domain.Member;
import com.community.world.dto.client.google.GoogleDto;
import com.community.world.dto.client.google.GoogleTokenDtoResponse;
import com.community.world.dto.jwt.ClientTokenDto;
import com.community.world.properties.GoogleClientProperties;
import lombok.Getter;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Objects;


@Getter
@Component
public class GoogleClient implements OauthClient {

    private final GoogleClientProperties googleClientProperties;
    private final ClientList client;

    public GoogleClient(GoogleClientProperties googleClientProperties) {
        this.googleClientProperties = googleClientProperties;
        this.client = ClientList.GOOGLE;
    }

    @Override
    public String oauthLoginRequest() {
        return getURI(googleClientProperties.getOauthUri(),
                googleClientProperties.getAhthQueryMap()).toString();
    }

    @Override
    public ClientTokenDto.Response requestAccessToken(String code, String state) {

        var body = googleClientProperties.getAccessTokenQueryMap(code, state);
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(body, headers);
        ResponseEntity<GoogleTokenDtoResponse> tokenResponse = restTemplate.exchange(
                        getURI(googleClientProperties.getTokenUri()),
                        HttpMethod.POST,
                        requestEntity,
                        GoogleTokenDtoResponse.class);

        String bearer = "Bearer ";
        RequestEntity<Void> userRequest = RequestEntity
                .get(getURI(googleClientProperties.getUserInfoUri()))
                .header(
                        HttpHeaders.AUTHORIZATION,
                        bearer
                                + Objects.requireNonNull(
                                        tokenResponse.getBody()
                                ).getAccessToken()
                )
                .build();

        System.out.println(bearer+tokenResponse.getBody().getAccessToken());

        ResponseEntity<GoogleDto.Response> userResponse =
                restTemplate.exchange(userRequest, GoogleDto.Response.class);
        var userInfo = userResponse.getBody();
        var member = Member.builder()
                .email(userInfo.getEmail())
                .name(userInfo.getName())
                .build();

        return ClientTokenDto.Response.builder()
                .accessToken(tokenResponse.getBody().getAccessToken())
                .member(member)
                .build();
    }
}
