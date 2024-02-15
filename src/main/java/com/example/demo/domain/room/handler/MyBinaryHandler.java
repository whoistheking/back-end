package com.example.demo.domain.room.handler;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.handler.BinaryWebSocketHandler;

@Component
@RequiredArgsConstructor
public class MyBinaryHandler extends BinaryWebSocketHandler {

}
