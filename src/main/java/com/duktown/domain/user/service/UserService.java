package com.duktown.domain.user.service;

import com.duktown.domain.user.dto.UserDto;
import com.duktown.domain.user.entity.User;
import com.duktown.domain.user.entity.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // 사용자 회원가입 메서드
    public void signup(UserDto.SignupRequest signupRequest) {
        // 이메일 중복 체크

        // 이메일 인증 여부 체크

        // 비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(signupRequest.getPassword());

        // 사용자 등록
        User user = signupRequest.toEntity(encodedPassword);
        userRepository.save(user);

        // TODO: 회원가입 응답 반환 로직 (현재 void 메서드)
    }
}
