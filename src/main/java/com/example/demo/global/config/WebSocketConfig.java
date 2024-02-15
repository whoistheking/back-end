package com.example.demo.global.config;

import com.example.demo.domain.room.handler.MyBinaryHandler;
import com.example.demo.domain.room.handler.MyTextHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
@RequiredArgsConstructor
public class WebSocketConfig implements WebSocketConfigurer {

    private final MyTextHandler webTextHandler;
    private final MyBinaryHandler webBinaryHandler;

    // 엔드포인트 설정 : /api/v1/room/{roomId}
    // ws://localhost:8080/ws/message 으로 요청이 들어오면 websocket 통신을 진행 & 모든 cors 요청 허용
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(webTextHandler, "ws/message").setAllowedOrigins("*");
        registry.addHandler(webBinaryHandler, "ws/game").setAllowedOrigins("*");
    }

}
