package com.example.demo.domain.room.entity;

import com.example.demo.domain.room.dto.GameMessage;
import com.example.demo.domain.room.service.RoomService;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.socket.WebSocketSession;

import java.util.Set;

@Getter
@NoArgsConstructor
@Entity
public class GameRoom {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String roomId;

    public GameRoom(String randomId) {
        this.roomId = randomId;
    }
}
