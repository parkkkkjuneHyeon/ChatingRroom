package com.community.world.controller.invitation;

import com.community.world.domain.Member;
import com.community.world.dto.chatingroom.ChatRoomDto;
import com.community.world.dto.invitatationMessage.InvitationDto;
import com.community.world.service.InvitationService;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api")
public class InvitationApiController {
    private final InvitationService invitationService;

    public InvitationApiController(
            InvitationService invitationService) {
        this.invitationService = invitationService;
    }

    @GetMapping("/invitations")
    public ResponseEntity<?> getInvitations(
            Authentication authentication) {
        log.info("invitation 진입");
        Member member = (Member)authentication.getPrincipal();
        return ResponseEntity.ok(invitationService
                .getInvitationMessage(member.getEmail()));
    }

    @Transactional
    @PostMapping("/invite-member")
    public ResponseEntity<?> addInvitationMessage(
            @RequestBody InvitationDto.Request request,
            Authentication authentication
    ) {
        log.info("invite-member 진입");
        var invitationDtoResponse = invitationService
                .addInviteMessage(
                        request,
                        (Member)authentication.getPrincipal());

        return ResponseEntity.ok(invitationDtoResponse);
    }

    @Transactional
    @DeleteMapping("/delete-invitation")
    public ResponseEntity<?> deleteInvitationMessage(
            @RequestBody ChatRoomDto.Request request,
            Authentication authentication) {
        log.info("delete-invitation 진입");
        log.info("룸 이름 : {}",request.getChatingName());
        log.info("룸 키 : {}",request.getRoomKey());
        invitationService
                .deleteInvitationMessage(
                        request,
                        (Member)authentication.getPrincipal());

        return ResponseEntity.ok(HttpStatus.OK);
    }

}
