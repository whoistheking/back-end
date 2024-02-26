package com.example.demo.domain.room.handler;

import com.example.demo.domain.room.dto.GameMessageDto;
import com.example.demo.domain.room.entity.Room;
import com.example.demo.domain.room.service.RoomService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.HashSet;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class MyTextHandler extends TextWebSocketHandler {

    private final ObjectMapper objectMapper; //json 데이터를 클래스에 맞게 파싱하여 객체로 변환
    private final RoomService roomService;

    private Set<WebSocketSession> sessions = new HashSet<>();

    //메세지 전송
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception{
        //페이로드에서 gameMessage(DTO)로 변환
        String payload = message.getPayload();
        GameMessageDto gameMessage = objectMapper.readValue(payload, GameMessageDto.class);

        //session 연결 & 메시지 전송 (경우의 수에 따라 다르게 처리)
        Room gameRoom = roomService.findRoomById(gameMessage.getRoomId());
        handlerActions(session, gameMessage, roomService);
    }
    public void handlerActions(WebSocketSession session, GameMessageDto gameMessage, RoomService roomService) {
        //현재 접속중인 상태인지 판별한 후 session 연결과 입장 메세지 전송
        if (gameMessage.getType().equals(GameMessageDto.MessageType.ENTER)){
            sessions.add(session);
            gameMessage.setMessage(gameMessage.getSender());
        }
        // 접속중인 사람이면 메세지 전송 (추후에 messagetype 에 따라 다르게 전송하도록 처리 필요)
        sendMessage(gameMessage, roomService);
    }
    private <T> void sendMessage(T message, RoomService roomService) {
        sessions.parallelStream()
                .forEach(session -> roomService.sendMessage(session, message));
    }
}
