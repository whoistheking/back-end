package com.example.demo.domain.ban.controller;

import com.example.demo.domain.ban.service.BanService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "예제 API", description = "Swagger 테스트용 API")
@RestController
@RequiredArgsConstructor
@RequestMapping
public class BanController {

    private final BanService banService;

    @PostMapping("/create")
    public ResponseEntity<?> ban() {
        return null;
    }
}
