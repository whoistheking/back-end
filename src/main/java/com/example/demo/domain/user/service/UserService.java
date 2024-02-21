package com.example.demo.domain.user.service;

import com.example.demo.domain.user.dto.LoginRequestDto;
import com.example.demo.domain.user.dto.ProfileResponseDto;
import com.example.demo.domain.user.entity.User;
import com.example.demo.domain.user.entity.UserRoleEnum;
import com.example.demo.domain.user.entity.UserSocialEnum;
import com.example.demo.domain.user.repository.UserRepository;
import com.example.demo.global.jwt.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;

    public ProfileResponseDto profile(User user) {
        return ProfileResponseDto.builder()
                .name(user.getName())
                .exp(user.getExp())
                .level(user.getLevel()).build();
    }

    //테스트용이라 나중에 삭제
    public ResponseEntity<?> login(LoginRequestDto loginRequestDto) {
        try {
            String uuid = UUID.randomUUID().toString();
            User user = new User(uuid, loginRequestDto.getEmail(), loginRequestDto.getPassword(), UserSocialEnum.NAVER, UserRoleEnum.USER);

            String accessToken = jwtUtil.createAccessToken(loginRequestDto.getPassword(), UserRoleEnum.USER);
            userRepository.save(user);

            return ResponseEntity.ok(accessToken);

        }
        catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    //로그인시 로그인된 정보를 db에 저장해야만 닉네임 변경이 가능
}
