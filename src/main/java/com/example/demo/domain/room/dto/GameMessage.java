package com.example.demo.domain.room.dto;

import lombok.Getter;

@Getter
public class GameMessage {

    public enum MessageType{
        ENTER, TALK, TURN
    }

    private MessageType messageType;
    private String roomId;
    private String sender;
    private String message;

    public void setMessage(String s) {
        this.message = s  + "님이 입장했습니다.";
    }

}
