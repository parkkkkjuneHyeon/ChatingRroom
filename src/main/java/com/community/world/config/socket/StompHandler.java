package com.community.world.config.socket;

import com.community.world.config.jwt.TokenService;
import com.community.world.domain.Member;
import com.community.world.service.MemberApiService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Slf4j
@Component
public class StompHandler implements ChannelInterceptor {

    private final TokenService tokenService;
    private final MemberApiService memberApiService;
    private final String AUTHORIZATION = "Authorization";
    private final String BEARER_PREFIX = "Bearer ";

    public StompHandler(
            TokenService tokenService,
            MemberApiService memberApiService) {
        this.tokenService = tokenService;
        this.memberApiService = memberApiService;
    }

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
        log.info("preSend 진입 : {}", accessor.getFirstNativeHeader(AUTHORIZATION));
        if(StompCommand.CONNECT.equals(accessor.getCommand())) {
            String authorization = accessor.getFirstNativeHeader(AUTHORIZATION);
            String accessToken = Objects.requireNonNull(authorization).substring(BEARER_PREFIX.length());

            String memberEmail = tokenService.getSubject(accessToken);
            var userDetails = memberApiService.loadUserByUsername(memberEmail);
            Member member = (Member)userDetails;

            var authentication = new UsernamePasswordAuthenticationToken(
                    userDetails, "", userDetails.getAuthorities());
            SecurityContext securityContext = SecurityContextHolder.getContext();
            securityContext.setAuthentication(authentication);
            SecurityContextHolder.setContext(securityContext);

            log.info("preSend 인증 완료 : {}", authentication.getPrincipal());
        }
        return message;
    }
}
