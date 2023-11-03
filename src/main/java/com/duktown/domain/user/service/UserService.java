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
import static com.duktown.global.exception.CustomErrorType.LOGIN_ID_ALREADY_EXIST;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;

    // 이메일 중복 체크 메서드
    @Transactional(readOnly = true)
    public UserDto.EmailCheckResponse emailCheck(UserDto.EmailCheckRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElse(null);

        if (user != null) {
            return new UserDto.EmailCheckResponse(true);
        }
        return new UserDto.EmailCheckResponse(false);
    }

    // 아이디 중복 체크 메서드
    @Transactional(readOnly = true)
    public UserDto.IdCheckResponse idCheck(UserDto.IdCheckRequest idCheckRequest) {
        User user = userRepository.findByLoginId(idCheckRequest.getLoginId())
                .orElse(null);

        if (user != null) {
            return new UserDto.IdCheckResponse(true);
        }
        return new UserDto.IdCheckResponse(false);
    }

    // 사용자 회원가입 메서드
    public UserDto.SignUpResponse signup(UserDto.SignupRequest signupRequest) {

        /*
        * 회원가입 이전 과정이 이메일, 아이디 중복 체크이지만,
        * 전 과정 없이 이 요청만 단독으로 들어오는 경우를 예방하기 위해 signup 요청때도 다시 한번 중복 체크를 수행한다.
        */
        // 이메일 중복 체크
        emailDuplicateCheck(signupRequest.getEmail());

        // 아이디 중복 체크
        idDuplicateCheck(signupRequest.getLoginId());

        // TODO: 이메일 인증 여부 체크

        // 비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(signupRequest.getPassword());

        // 사용자 등록
        User user = signupRequest.toEntity(encodedPassword);
        userRepository.save(user);

        String accessToken = jwtTokenProvider.createAccessToken(user.getLoginId(), user.getId());
        String refreshToken = jwtTokenProvider.createRefreshToken(user.getLoginId(), user.getId());
        user.updateRefreshToken(refreshToken);
        return new UserDto.SignUpResponse(accessToken, refreshToken);
    }

    private void emailDuplicateCheck(String email) {
        userRepository.findByEmail(email)
                .ifPresent(user -> {
                    throw new CustomException(EMAIL_ALREADY_EXIST);
                });
    }

    private void idDuplicateCheck(String loginId) {
        userRepository.findByLoginId(loginId)
                .ifPresent(user -> {
                    throw new CustomException(LOGIN_ID_ALREADY_EXIST);
                });
    }
}
