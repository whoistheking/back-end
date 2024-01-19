package com.example.demo.domain.user.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "예제 API", description = "Swagger 테스트용 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    //로그아웃
    @PostMapping("/logout")
    public ResponseEntity<?> logout() {
        return null;
    }

    //내 프로필
    @GetMapping("/profile")
    public ResponseEntity<?> profile() {
        return null;
    }

    //내 프로필 닉네임만 변경 가능
    @PatchMapping("/modify/nickname")
    public ResponseEntity<?> modifyNickname() {
        return null;
    }
}
