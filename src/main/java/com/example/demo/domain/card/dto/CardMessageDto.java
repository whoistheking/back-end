package com.example.demo.domain.card.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CardMessageDto {

    public enum MessageType {
        CHECK, DISTINCT, PICK, SHUFFLE, ENDTURN
    }

    private String roomId;
    private String sender;
    private String content;
    private MessageType type;



}
