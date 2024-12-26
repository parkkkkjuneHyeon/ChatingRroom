package com.community.world.dto.message;

import com.community.world.domain.Message;
import lombok.*;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;

public class MessageDto {

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Request {
        private String memberEmail;
        private String chatingRoomName;
        private String memberName;
        private String chatingRoomKey;
        private String message;
        private String accessToken;
        public static Request getMessageDtoRequest(
                Message message
        ) {
            return Request.builder()
                    .memberEmail(message.getMemberEmail())
                    .chatingRoomName(message.getChatingRoomName())
                    .memberName(message.getMemberName())
                    .chatingRoomKey(message.getChatingRoomKey())
                    .message(message.getMessage())
                    .build();
        }
    }

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Response implements Comparable<Response>{
        private String memberEmail;
        private String chatingRoomName;
        private String memberName;
        private String chatingRoomKey;
        private String message;
        private ZonedDateTime createAt;
        public static Response getMessageDtoResponse(
                Message message
        ) {
            return Response.builder()
                    .memberEmail(message.getMemberEmail())
                    .chatingRoomName(message.getChatingRoomName())
                    .memberName(message.getMemberName())
                    .chatingRoomKey(message.getChatingRoomKey())
                    .message(message.getMessage())
                    .createAt(message.getCreateAt())
                    .build();
        }

        @Override
        public int compareTo(Response o) {
            return this.createAt.compareTo(o.getCreateAt());
        }
    }
}
