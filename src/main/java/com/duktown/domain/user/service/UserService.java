package com.duktown.domain.user.service;

import com.duktown.domain.user.dto.UserDto;
import com.duktown.domain.user.entity.User;
import com.duktown.domain.user.entity.UserRepository;
import com.duktown.global.exception.CustomException;
import com.duktown.global.security.provider.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.duktown.global.exception.CustomErrorType.EMAIL_ALREADY_EXIST;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final JwtTokenProvider jwtService;
    private final PasswordEncoder passwordEncoder;

    // 이메일 중복 체크 메서드
    @Transactional(readOnly = true)
    public UserDto.EmailCheckResponse emailDuplicateCheck(UserDto.EmailCheckRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElse(null);

        if (user != null) {
            return new UserDto.EmailCheckResponse(true);
        }
        return new UserDto.EmailCheckResponse(false);
    }

    // 사용자 회원가입 메서드
    public UserDto.SignUpResponse signup(UserDto.SignupRequest signupRequest) {
        // 이메일 중복 체크
        emailDuplicateCheck(signupRequest.getEmail());

        // 이메일 인증 여부 체크

        // 비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(signupRequest.getPassword());

        // 사용자 등록
        User user = signupRequest.toEntity(encodedPassword);
        userRepository.save(user);

        String accessToken = jwtService.createAccessToken(user.getEmail(), user.getId(), user.getRoleType());
        String refreshToken = jwtService.createRefreshToken(user.getEmail(), user.getId(), user.getRoleType());
        user.updateRefreshToken(refreshToken);
        return new UserDto.SignUpResponse(accessToken, refreshToken);
    }

    private void emailDuplicateCheck(String email) {
        userRepository.findByEmail(email)
                .ifPresent(user -> {
                    throw new CustomException(EMAIL_ALREADY_EXIST);
                });
    }
}
