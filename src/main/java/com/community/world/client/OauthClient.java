package com.community.world.client;

import com.community.world.dto.jwt.ClientTokenDto;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

public interface OauthClient {

    ClientList getClient();
    String oauthLoginRequest();
    ClientTokenDto.Response requestAccessToken(String code, String state);

    default URI getURI(String uri) {
        return UriComponentsBuilder
                .fromHttpUrl(uri)
                .encode()
                .build()
                .toUri();
    }

    default URI getURI(String uri, MultiValueMap<String, String> map) {
        return UriComponentsBuilder
                .fromHttpUrl(uri)
                .queryParams(map)
                .encode()
                .build()
                .toUri();
    }
}
