package com.community.world.controller.chatringroom;

import com.community.world.domain.Member;
import com.community.world.dto.chatingroom.ChatRoomDto;
import com.community.world.service.ChatApiService;
import com.community.world.service.InvitationService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api")
public class ChatApiController {

    private final ChatApiService chatApiService;
    private final InvitationService invitationService;
    public ChatApiController(
            ChatApiService chatApiService,
            InvitationService invitationService) {
        this.chatApiService = chatApiService;
        this.invitationService = invitationService;
    }

    @PostMapping("/add-chatingroom")
    public ResponseEntity<?> createChatingRoom(
            @Valid @RequestBody ChatRoomDto.Request request,
            Authentication authentication
    ) {
        Member member = (Member) authentication.getPrincipal();
        log.info("add-chatingroom 진입 이메일 : {}", member.getEmail());
        request.setMemberEmail(member.getEmail());
        chatApiService.addChatingRoom(request);

        return ResponseEntity.ok(HttpStatus.OK);
    }
    @GetMapping("/myrooms")
    public ResponseEntity<?> getMyChatingRooms(
            ChatRoomDto.Request request,
            Authentication authentication
    ) {
        Member member = (Member)authentication.getPrincipal();
        log.info("myrooms 진입 이메일 : {}", member.getEmail());
        return ResponseEntity
                .ok(chatApiService.searchChatingRoom(
                        request,
                        member));
    }

    @Transactional
    @PostMapping("/join-chatingroom")
    public ResponseEntity<?> joinChatingRoom(
        @RequestBody ChatRoomDto.Request request,
        Authentication authentication
    ){
        Member member = (Member) authentication.getPrincipal();
        log.info("join-chatingRoom 진입 이메일 : {}", member.getEmail());
        request.setMemberEmail(member.getEmail());
            var charRoomDtoResponse = chatApiService
                    .joinChatingRoom(request);
            //초대에 수락 후 초대테이블에 메시지를 삭제
            invitationService.deleteInvitationMessage(request,
                    (Member) authentication.getPrincipal());
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @Transactional
    @PatchMapping("/update-chatingroom")
    public ResponseEntity<?> updateChatingRoom(
            @Valid @RequestBody ChatRoomDto.Request request,
            Authentication authentication
    ){
        chatApiService.updateChatingRoom(request);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @Transactional
    @DeleteMapping("/delete-chatingroom")
    public ResponseEntity<?> deleteChatingRoom(
            @Valid @RequestBody ChatRoomDto.Request request,
            Authentication authentication
    ) {
        Member member = (Member) authentication.getPrincipal();
        request.setMemberEmail(member.getEmail());
        chatApiService.deleteChatingRoom(request);

        return ResponseEntity.ok(HttpStatus.OK);
    }

}
