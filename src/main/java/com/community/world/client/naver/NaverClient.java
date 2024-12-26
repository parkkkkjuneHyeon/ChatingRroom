package com.community.world.client.naver;

import com.community.world.client.ClientList;
import com.community.world.client.OauthClient;
import com.community.world.domain.Member;
import com.community.world.dto.client.naver.NaverTokenDtoResponse;
import com.community.world.dto.client.naver.NaverUserDto;
import com.community.world.dto.jwt.ClientTokenDto;
import com.community.world.properties.NaverClientProperties;
import lombok.Getter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.Objects;


@Getter
@Component
public class NaverClient implements OauthClient {

    private final NaverClientProperties naverClientProperties;
    private final ClientList client;

    public NaverClient(NaverClientProperties naverClientProperties) {
        this.naverClientProperties = naverClientProperties;
        this.client = ClientList.NAVER;
    }


    @Override
    public String oauthLoginRequest() {
        return getURI(naverClientProperties.getOathUri(),
                naverClientProperties.getAhthQueryMap()).toString();
    }

    @Override
    public ClientTokenDto.Response requestAccessToken(String code, String state) {
        var map = naverClientProperties.getAccessTokenQueryMap(code, state);
        URI tokenURI = getURI(naverClientProperties.getToken1Uri(), map);
        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<NaverTokenDtoResponse> tokenResponse = restTemplate
                .getForEntity(tokenURI, NaverTokenDtoResponse.class);

        String bearer = "bearer ";

        RequestEntity<Void> userRequest = RequestEntity
                .get(getURI(naverClientProperties.getUserInfo()))
                .header(HttpHeaders.AUTHORIZATION,
                        bearer + Objects
                                .requireNonNull(tokenResponse.getBody())
                                .getAccessToken())
                .build();

        ResponseEntity<NaverUserDto.Response> userResponse =
                restTemplate.exchange(userRequest,
                        NaverUserDto.Response.class);

        var userInfo = userResponse.getBody().getUser();
        var member = Member.builder()
                .email(userInfo.getEmail())
                .name(userInfo.getName())
                .password("1234")
                .build();


        return ClientTokenDto.Response.builder()
                .accessToken(tokenResponse.getBody().getAccessToken())
                .member(member)
                .build();
    }


}
