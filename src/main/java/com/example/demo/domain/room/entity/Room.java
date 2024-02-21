package com.example.demo.domain.room.entity;

import com.example.demo.domain.user.entity.User;
import com.example.demo.global.utils.Timestamped;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
public class Room extends Timestamped {

    @Id
    @Column(name = "room_id")
    private String roomId;

    @Column(name = "headcount")
    private Long headCount;

    @Column(name = "room_manager")
    private String roomManager; //userid를 넣을거 같은데

    @Column(name = "matching")
    private Boolean matching;

    @Column(name = "created")
    private LocalDateTime created;

//    @OneToMany(mappedBy = "user_id", cascade = CascadeType.ALL)
//    private List<User> user;      //에러떠요 ㅠ 나중에 안되는 이유 확인하기

    public Room(String roomId, String userId, Boolean type) {
        this.roomId = roomId;
        this.headCount = 0L;
        this.roomManager = userId;
        this.matching = type;
    }

    public void joinRoom() {
        this.headCount = getHeadCount() + 1L;
    }

    public void changeManager(String userId) {
        this.roomManager = userId;
    }
}
