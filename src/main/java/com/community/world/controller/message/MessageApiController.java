package com.community.world.controller.message;

import com.community.world.config.jwt.TokenService;
import com.community.world.domain.Member;
import com.community.world.dto.message.MessageDto;
import com.community.world.service.MemberApiService;
import com.community.world.service.MessageService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
@Slf4j
@Controller
public class MessageApiController {
    private final MessageService messageService;
    private final MemberApiService memberApiService;
    private final TokenService tokenService;

    public MessageApiController(
            MessageService messageService,
            MemberApiService memberApiService,
            TokenService tokenService) {
        this.messageService = messageService;
        this.memberApiService = memberApiService;
        this.tokenService = tokenService;
    }


    @MessageMapping("/send-Message")
    @SendTo("/topic/messages")
    public MessageDto.Response sendMessage(
            @Valid @RequestBody MessageDto.Request request
    ) {
        log.info("/send-Message 진입 성공");
        var userDetails = memberApiService
                .loadUserByUsername(tokenService.getSubject(request.getAccessToken()));
        Member member = (Member) userDetails;
        request.setMemberEmail(member.getEmail());
        request.setMemberName(member.getName());
        printLog("/send-Message", request);
        return messageService.addMessage(request);
    }
    @PostMapping("/search-Messages")
    @ResponseBody
    public List<MessageDto.Response> searchMessages(
            @Valid @RequestBody MessageDto.Request request
    ){
        printLog("/search-Messages" , request);

        return messageService.searchMessages(request);
    }

    public void printLog(String url, MessageDto.Request request) {
        log.info("{} 진입 이메일 : {}", url, request.getMemberEmail());
        log.info("이름 : {}", request.getMemberName());
        log.info("채팅방 : {}", request.getChatingRoomName());
        log.info("룸키 : {}", request.getChatingRoomKey());
    }
}
