package com.community.world.dto.invitatationMessage;

import com.community.world.domain.InvitationMessage;
import lombok.*;

public class InvitationDto {

    @Setter
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Request{
        private String fromMemberEmail;
        private String toMemberEmail;
        private String chatingRoomName;
        private String chatingRoomKey;

        public static InvitationMessage getInvitationMessage(
                InvitationDto.Request request) {
            System.out.println(request.getChatingRoomName());
            return InvitationMessage.builder()
                    .fromMemberEmail(request.getFromMemberEmail())
                    .toMemberEmail(request.getToMemberEmail())
                    .chatingRoomName(request.getChatingRoomName())
                    .chatingRoomKey(request.getChatingRoomKey())
                    .build();
        }

    }

    @Setter
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Response {
        private String fromMemberEmail;
        private String toMemberEmail;
        private String chatingRoomName;
        private String chatingRoomKey;

        public static Response getInvitateDtoResponse(
                InvitationMessage invitationMessage) {
            return Response.builder()
                    .fromMemberEmail(invitationMessage.getFromMemberEmail())
                    .toMemberEmail(invitationMessage.getToMemberEmail())
                    .chatingRoomName(invitationMessage.getChatingRoomName())
                    .chatingRoomKey(invitationMessage.getChatingRoomKey())
                    .build();
        }
    }
}

