package com.example.demo.domain.user.entity;

import com.example.demo.domain.card.entity.Card;
import com.example.demo.domain.room.entity.Room;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import java.util.List;
import java.util.UUID;

import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
public class User {

    @Id
    @Column(name = "user_id")
    private String userId = String.valueOf(UUID.randomUUID());

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private UserRoleEnum role;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private UserSocialEnum social;

    @Column(nullable = false)
    private Long level;

    @Column(nullable = false)
    private Long exp;

    @Column(nullable = true)
    private boolean ready;

    @Column(nullable = true)
    private boolean done;

    //게임중인지도 판별해서 넣어줄건가요?

//    @OneToMany
//    @JoinColumn(name = "card_id")
//    private List<Card> cards;

    @ManyToOne
    @JoinColumn(name = "room_id")
    private Room room;

    public User(String userId, String name, String email, UserSocialEnum userSocialEnum, UserRoleEnum roleEnum) {
        userId = userId;        //임시로 바꿨음 원복해둘것
        this.name = name;
        this.email = email;
        this.level = 0L;
        this.exp = 0L;
        this.social = userSocialEnum;
        this.role = roleEnum;
    }

    public void joinUser(Room room) {
        this.room = room;
    }

    public void setReady() {
        this.ready = true;
    }

    public void setDone() {
        this.done = true;
    }
}
