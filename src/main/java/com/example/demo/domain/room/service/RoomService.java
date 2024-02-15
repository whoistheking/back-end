package com.example.demo.domain.room.service;

import com.example.demo.domain.room.entity.GameRoom;
import com.example.demo.domain.room.repository.RoomRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Service
public class RoomService {
    private final ObjectMapper objectMapper;
    private final RoomRepository roomRepository;

    public ResponseEntity<?> createRoom() {
        String randomId = UUID.randomUUID().toString();
        GameRoom gameRoom = new GameRoom(randomId);
        roomRepository.save(gameRoom);
        return ResponseEntity.ok(gameRoom);
    }

    public GameRoom findRoomById(String roomId) {
        return roomRepository.findByRoomId(roomId);
    }

    public <T> void sendMessage(WebSocketSession session, T message) {
        try{
            session.sendMessage(new TextMessage(objectMapper.writeValueAsString(message)));
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }
}
