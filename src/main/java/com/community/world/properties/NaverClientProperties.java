package com.community.world.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@Getter
@Setter
@Component
public class NaverClientProperties {
    @Value("${spring.security.oauth2.client.provider.naver.authorization-uri}")
    private String oathUri;

    @Value("${spring.security.oauth2.client.provider.naver.token-uri}")
    private String token1Uri;

    @Value("${spring.security.oauth2.client.provider.naver.user-info-uri}")
    private String userInfo;

    @Value("${spring.security.oauth2.client.registration.naver.client_id}")
    private String clientId;

    @Value("${spring.security.oauth2.client.registration.naver.client_secret}")
    private String clientSecret;
    //인증 과정에 대한 구분값
    //1) 발급:'authorization_code 2) 갱신:'refresh_token'3) 삭제: 'delete'
    private String grantType;
    private String responseType = "code";
    private String state = "STATE_STRING";
    private String redirectUri = "http://localhost:8080/api/oauth/naver/callback";

    public MultiValueMap<String, String> getAccessTokenQueryMap(String code, String state) {
        MultiValueMap<String , String> map = new LinkedMultiValueMap<>();
        map.add("grant_type", "authorization_code");
        map.add("client_id", clientId);
        map.add("client_secret", clientSecret);
        map.add("code", code);
        map.add("state", state);
        return map;
    }

    public MultiValueMap<String, String> getAhthQueryMap() {
        MultiValueMap<String , String> map = new LinkedMultiValueMap<>();
        map.add("response_type", responseType);
        map.add("client_id", clientId);
        map.add("state",state);
        map.add("redirect_uri",redirectUri);

        return map;
    }
}
