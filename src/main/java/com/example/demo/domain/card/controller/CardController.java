package com.example.demo.domain.card.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "예제 API", description = "Swagger 테스트용 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/card")
public class CardController {

    //카드 조회
    @GetMapping("/share")
    public ResponseEntity<?> share() {
        return null;
    }

    //중복 제거(시작시)
    @PostMapping("/start/distinct")
    public ResponseEntity<?> startDistinct() {
        return null;
    }

    //카드 뽑기 & 카드 중복 제거 & 종료 & 경험치
    @PostMapping("/distinct")
    public ResponseEntity<?> distinct() {
        return null;
    }

    //카드 뽑을때 섞기
    @PostMapping("/shuffle")
    public ResponseEntity<?> shuffle() {
        return null;
    }
}
