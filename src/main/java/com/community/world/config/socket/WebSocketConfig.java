package com.community.world.config.socket;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
@Order(Ordered.HIGHEST_PRECEDENCE + 99)
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
    private final StompHandler stompHandler;

    public WebSocketConfig(StompHandler stompHandler) {
        this.stompHandler = stompHandler;
    }

    @Override
    public void configureMessageBroker(
            MessageBrokerRegistry config) {
        //가공이 필요없는 데이터일 경우 /topic경로로 보내서 stompBrokerRelay를 통해서 응답함.
        config.enableSimpleBroker("/topic");
        // /app경로로 보내면 메시지를 가공해서 stompBrokerRelay를 통해 응답을 보냄
        config.setApplicationDestinationPrefixes("/app");
    }
    @Override
    public void registerStompEndpoints(
            StompEndpointRegistry registry) {
        //Opening Handshake가 끝나면 ws로 프로토콜을 변환하여 커넥션을 맺음.
        //WebSocket을 지원하지 않은 브라우저에서도 동작할 수 있게함.
        registry.addEndpoint("/ws")
                .withSockJS();
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(stompHandler);
    }
}
