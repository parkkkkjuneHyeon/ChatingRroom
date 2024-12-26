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
public class GoogleClientProperties {

    @Value("${spring.security.oauth2.client.provider.google.authorization-uri}")
    private String oauthUri;

    @Value("${spring.security.oauth2.client.provider.google.token-uri}")
    private String tokenUri;

    @Value("${spring.security.oauth2.client.provider.google.user-info-uri}")
    private String userInfoUri;

    @Value("${spring.security.oauth2.client.registration.google.client_id}")
    private String clientId;
    @Value("${spring.security.oauth2.client.registration.google.client_secret}")
    private String clientSecret;
    private String scope = "https://www.googleapis.com/auth/cloud-platform";
    private String responseType = "code";
    private String state;
    private String grantType;
    private String redirectUri = "http://localhost:8080/api/oauth/google/callback";


    public MultiValueMap<String, String> getAhthQueryMap() {
        MultiValueMap<String , String> map = new LinkedMultiValueMap<>();
        map.add("client_id", clientId);
        map.add("redirect_uri", redirectUri);
        map.add("response_type", responseType);
        map.add("scope", scope);
        map.add("include_granted_scopes", "true");
        map.add("access_type", "offline");
        return map;
    }
    public MultiValueMap<String, String> getAccessTokenQueryMap(String code, String state) {
        MultiValueMap<String , String> map = new LinkedMultiValueMap<>();
        map.add("grant_type", "authorization_code");
        map.add("client_id", clientId);
        map.add("client_secret", clientSecret);
        map.add("code", code);
        map.add("redirect_uri", redirectUri);
        return map;
    }


}
