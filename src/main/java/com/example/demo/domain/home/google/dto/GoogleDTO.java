package com.example.demo.domain.home.google.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class GoogleDTO {

    private String id;
    private String email;
    private String nickname;

}
