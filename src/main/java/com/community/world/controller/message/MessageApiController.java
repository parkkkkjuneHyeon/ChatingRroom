package com.community.world.controller.message;

import com.community.world.config.jwt.TokenService;
import com.community.world.domain.Member;
import com.community.world.dto.message.MessageDto;
import com.community.world.service.MemberApiService;
import com.community.world.service.MessageService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@Slf4j
@RestController
public class MessageApiController {
    private final MessageService messageService;
    private final MemberApiService memberApiService;
    private final TokenService tokenService;
    private final SimpMessagingTemplate messagingTemplate;

    public MessageApiController(
            MessageService messageService,
            MemberApiService memberApiService,
            TokenService tokenService,
            SimpMessagingTemplate messagingTemplate) {
        this.messageService = messageService;
        this.memberApiService = memberApiService;
        this.tokenService = tokenService;
        this.messagingTemplate = messagingTemplate;
    }


    @MessageMapping("/send-message")
    public void sendMessage(
            @Valid @Payload MessageDto.Request request
    ) {
        log.info("/send-Message 진입 성공");
        var userDetails = memberApiService
                .loadUserByUsername(tokenService.getSubject(request.getAccessToken()));

        Member member = (Member) userDetails;
        request.setMemberEmail(member.getEmail());
        request.setMemberName(member.getName());

        printLog("/send-message", request);

        var savedMessage = messageService.addMessage(request);
        String destination = "/topic/messages/" + request.getChatingRoomKey();
        messagingTemplate.convertAndSend(destination, savedMessage);
        log.info("destination : " + destination );
    }
    @PostMapping("/search-messages")
    public List<MessageDto.Response> searchMessages(
            @Valid @RequestBody MessageDto.Request request
    ){
        printLog("/search-messages" , request);

        return messageService.searchMessages(request);
    }

    public void printLog(String url, MessageDto.Request request) {
        log.info("{} 진입 이메일 : {}", url, request.getMemberEmail());
        log.info("이름 : {}", request.getMemberName());
        log.info("채팅방 : {}", request.getChatingRoomName());
        log.info("룸키 : {}", request.getChatingRoomKey());
    }
}
