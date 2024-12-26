package com.community.world.dto.client.kakao;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

public class KakaoDto {

    @Getter
    @Setter
    public static class Response {
        @JsonProperty("kakao_account")
        private Response.User User;

        @Getter
        @Setter
        public class User {

            private String profile;
            private String name;
            private String email;
            private String age_range;
            @JsonProperty("birthyear")
            private String birthYear;
            private String birthDay;
            private String gender;
        }


    }
}
