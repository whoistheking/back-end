package com.example.demo.domain.room.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "예제 API", description = "Swagger 테스트용 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/room")
public class RoomController {

    //사설 방 생성
    @PostMapping("/create")
    public ResponseEntity<?> create() {
        return null;
    }

    //자동 매칭 시스템
    @PostMapping("/match")
    public ResponseEntity<?> match() {
        return null;
    }

    //방 코드를 사용한 입장
    @PostMapping("/join")
    public ResponseEntity<?> join() {
        return null;
    }

    //게임 준비 버튼(방장 제외)
    @PostMapping("/ready")
    public ResponseEntity<?> ready() {
        return null;
    }

    //게임 시작 & 53장 카드 배분
    @PostMapping("/start")
    public ResponseEntity<?> start() {
        return null;
    }

    //방 나가기(사람이 없을시 방 폭파)(방장 넘기기 랜덤)
    @PostMapping("/leave")
    public ResponseEntity<?> leave() {
        return null;
    }
}
