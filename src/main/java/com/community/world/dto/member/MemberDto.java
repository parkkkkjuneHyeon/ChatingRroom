package com.community.world.dto.member;

import com.community.world.domain.Member;
import lombok.*;


public class MemberDto {


    @AllArgsConstructor
    @NoArgsConstructor
    @Setter
    @Getter
    @Builder
    public static class Request {
        private Long id;
        private String email;
        private String password;
        private String name;
        private int age;
        private String gender;

        public static Request getMemberRequest(
                Member member
        ) {
            return Request.builder()
                    .id(member.getId())
                    .email(member.getEmail())
                    .password(member.getPassword())
                    .name(member.getName())
                    .age(member.getAge())
                    .gender(member.getGender())
                    .build();
        }
    }

    @Setter
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Response {
        private Long id;
        private String email;
        private String name;
        private int age;
        private String gender;

        public static Response getMemberResponse(
                Member member
        ) {
            return Response.builder()
                    .id(member.getId())
                    .email(member.getEmail())
                    .name(member.getName())
                    .gender(member.getGender())
                    .age(member.getAge())
                    .build();
        }
    }
}
