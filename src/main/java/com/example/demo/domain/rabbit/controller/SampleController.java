package com.example.demo.domain.rabbit.controller;

//메시지를 발행해주는 컨트롤러

import com.example.demo.domain.room.dto.GameMessageDto;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class SampleController {
    private static final String EXCHANGE_NAME = "sample.exchange";
    private final RabbitTemplate rabbitTemplate;


    @GetMapping("/sample/queue")
    public String samplePublish() {
        rabbitTemplate.convertAndSend(EXCHANGE_NAME, "sample.jiwon", "RabbitMQ + Springboot = Success!");
        return "message sending!";
    }

    // 입장
    @MessageMapping("chat.enter.{roomId}")
    public String enterUser() {
        rabbitTemplate.convertAndSend(EXCHANGE_NAME, "sample.jiwon", "RabbitMQ + Springboot = Success!");
        return "message sending!";
    }


}
