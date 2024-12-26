package com.community.world.dto.member;

import com.community.world.domain.Member;
import lombok.*;

public class MemberUpdateDto {
    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Request {
        private Long id;
        private String email;
        private String updateEmail;
        private String updatePassword;
        private String name;
        private int age;
    }
    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Response {
        private Long id;
        private String email;
        private String name;
        private int age;

        public static MemberUpdateDto.Response getMemberUpdateDto(Member member) {
            return MemberUpdateDto.Response.builder()
                    .id(member.getId())
                    .email(member.getEmail())
                    .name(member.getName())
                    .age(member.getAge())
                    .build();
        }
    }
}
