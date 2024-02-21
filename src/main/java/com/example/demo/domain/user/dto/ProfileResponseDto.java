package com.example.demo.domain.user.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ProfileResponseDto {

    private String name;
    private Long exp;
    private Long level;
    //이미지 고민중
}
