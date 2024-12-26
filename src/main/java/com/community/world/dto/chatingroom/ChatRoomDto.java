package com.community.world.dto.chatingroom;

import com.community.world.domain.ChatingRoom;
import lombok.*;


public class ChatRoomDto {
    @ToString
    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Request {
        private String memberEmail;
        private String chatingName;
        private String roomKey;
    }
    @ToString
    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Response {
        private String memberEmail;
        private String chatingName;
        private String roomKey;

        public static Response getChatRoomDto(
                ChatingRoom chatingRoom
        ) {
            return Response.builder()
                    .memberEmail(chatingRoom.getMemberEmail())
                    .chatingName(chatingRoom.getChatingName())
                    .roomKey(chatingRoom.getRoomKey())
                    .build();
        }
    }
}
