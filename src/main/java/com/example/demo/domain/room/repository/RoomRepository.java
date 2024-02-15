package com.example.demo.domain.room.repository;

import com.example.demo.domain.room.entity.GameRoom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomRepository extends JpaRepository<GameRoom, Long> {
    GameRoom findByRoomId(String roomId);
}
