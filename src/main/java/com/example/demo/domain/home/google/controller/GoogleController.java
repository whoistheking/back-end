package com.example.demo.domain.home.google.controller;

import com.example.demo.domain.home.common.MsgEntity;
import com.example.demo.domain.home.google.dto.GoogleDTO;
import com.example.demo.domain.home.google.service.GoogleService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("google")
public class GoogleController {

    private final GoogleService googleService;

    @GetMapping("/callback")
    public ResponseEntity<MsgEntity> callback(HttpServletRequest request) throws Exception {
        GoogleDTO googleInfo = googleService.getGoogleInfo(request.getParameter("code"));

        return ResponseEntity.ok()
                .body(new MsgEntity("Success", googleInfo));
    }
}
