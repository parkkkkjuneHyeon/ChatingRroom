package com.community.world.dto.client.google;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class GoogleDto {

    @Getter
    @Setter
    @NoArgsConstructor
    public static class Response {
        private String email;
        private String name;
        @JsonProperty("given_name")
        private String givenName;
        @JsonProperty("family_name")
        private String familyName;
    }
}
