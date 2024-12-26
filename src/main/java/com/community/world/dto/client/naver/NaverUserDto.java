package com.community.world.dto.client.naver;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

public class NaverUserDto {


    @Getter
    public static class Response {
        @JsonProperty("response")
        private User User;

        @Getter
        @Setter
        public class User {
            private String age;
            private String gender;
            private String email;
            private String name;
            @JsonProperty("birthyear")
            private String birthYear;
        }
    }


}
