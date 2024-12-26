package com.community.world.client.kakao;


import com.community.world.client.ClientList;
import com.community.world.client.OauthClient;
import com.community.world.domain.Member;
import com.community.world.dto.client.kakao.KakaoDto;
import com.community.world.dto.client.kakao.KakaoTokenDtoResponse;
import com.community.world.dto.jwt.ClientTokenDto;
import com.community.world.properties.KakaoClientProperties;
import lombok.Getter;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;


@Getter
@Component
public class KakaoClient implements OauthClient {

    private final KakaoClientProperties kakaoClientProperties;
    private final ClientList client;

    public KakaoClient(KakaoClientProperties kakaoClientProperties) {
        this.kakaoClientProperties = kakaoClientProperties;
        this.client = ClientList.KAKAO;
    }

    @Override
    public String oauthLoginRequest() {
        return getURI(kakaoClientProperties.getOauthUri(),
                kakaoClientProperties.getAhthQueryMap()).toString();
    }

    @Override
    public ClientTokenDto.Response requestAccessToken(String code, String state) {


        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(
                kakaoClientProperties.getAccessTokenQueryMap(code, state), headers);

        ResponseEntity<KakaoTokenDtoResponse> tokenResponse = restTemplate.exchange(
                        getURI(kakaoClientProperties.getTokenUri()),
                        HttpMethod.POST,
                        requestEntity,
                        KakaoTokenDtoResponse.class);

        String bearer = "Bearer ";
        RequestEntity<Void> userRequest = RequestEntity
                .get(getURI(kakaoClientProperties.getUserInfoUri()))
                .header(
                        HttpHeaders.AUTHORIZATION,
                        bearer+tokenResponse.getBody().getAccessToken())
                .build();

        System.out.println(bearer+tokenResponse.getBody().getAccessToken());

        ResponseEntity<KakaoDto.Response> userResponse =
                restTemplate.exchange(userRequest, KakaoDto.Response.class);
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
