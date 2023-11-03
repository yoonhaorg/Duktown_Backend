package com.duktown.unit.user.service;

import com.duktown.domain.user.dto.UserDto;
import com.duktown.domain.user.entity.User;
import com.duktown.domain.user.entity.UserRepository;
import com.duktown.domain.user.service.UserService;
import com.duktown.global.exception.CustomException;
import com.duktown.global.security.provider.JwtTokenProvider;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.nio.charset.StandardCharsets;
import java.util.Optional;

import static com.duktown.global.exception.CustomErrorType.EMAIL_ALREADY_EXIST;
import static com.duktown.global.exception.CustomErrorType.LOGIN_ID_ALREADY_EXIST;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Spy
    @InjectMocks
    UserService userService;

    @Mock
    UserRepository userRepository;

    @Mock
    PasswordEncoder passwordEncoder;

    @Mock
    JwtTokenProvider jwtTokenProvider;

    private User createMockUser(Long id) {
        PasswordEncoder encoder = new BCryptPasswordEncoder();

        return User.builder()
                .id(id)
                .email("dsStudent" + id + "@duksung.ac.kr")
                .loginId("dsStudent" + id)
                .password(encoder.encode("1234"))
                .build();
    }

    @DisplayName("이메일 중복 체크 성공 - 중복 이메일 존재 x")
    @Test
    void duplicatedEmailNotExist_success(){
        // given
        given(userRepository.findByEmail(any())).willReturn(Optional.empty());
        UserDto.EmailCheckRequest request = new UserDto.EmailCheckRequest("dskim@duksung.ac.kr");

        // when
        UserDto.EmailCheckResponse response = userService.emailCheck(request);

        //then
        assertThat(response.getIsDuplicated()).isEqualTo(false);
    }

    @DisplayName("이메일 중복 체크 성공 - 중복 이메일 존재")
    @Test
    void duplicatedEmailExist_success() {
        // given
        User user = createMockUser(1L);
        given(userRepository.findByEmail(any())).willReturn(Optional.of(user));
        UserDto.EmailCheckRequest request = new UserDto.EmailCheckRequest(user.getEmail());

        // when
        UserDto.EmailCheckResponse response = userService.emailCheck(request);

        // then
        assertThat(response.getIsDuplicated()).isEqualTo(true);
    }

    @DisplayName("아이디 중복 체크 성공 - 중복 아이디 존재 x")
    @Test
    void duplicatedLoginIdNotExist_success() {
        // given
        given(userRepository.findByLoginId(any())).willReturn(Optional.empty());
        UserDto.IdCheckRequest request = new UserDto.IdCheckRequest("dskim");

        // when
        UserDto.IdCheckResponse response = userService.idCheck(request);

        // then
        assertThat(response.getIsDuplicated()).isEqualTo(false);
    }

    @DisplayName("아이디 중복 체크 성공 - 중복 아이디 존재")
    @Test
    void duplicatedLoginIdExist_success() {
        // given
        User user = createMockUser(1L);
        given(userRepository.findByLoginId(any())).willReturn(Optional.of(user));
        UserDto.IdCheckRequest request = new UserDto.IdCheckRequest(user.getLoginId());

        // when
        UserDto.IdCheckResponse response = userService.idCheck(request);

        // then
        assertThat(response.getIsDuplicated()).isEqualTo(true);
    }

    @DisplayName("회원가입 성공")
    @Test
    void signup_success() {
        // given
        JwtTokenProvider tokenProvider = new JwtTokenProvider(userRepository);
        String secretKey = "temporary_secret_key_for_this_signup_test";
        tokenProvider.jwtSecretKey = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
        PasswordEncoder encoder = new BCryptPasswordEncoder();

        UserDto.SignupRequest request =
                new UserDto.SignupRequest(
                        "김덕성", "dskim@duksung.ac.kr", "dskim", encoder.encode("1234"));

        doReturn(
                new UserDto.SignUpResponse(
                        tokenProvider.createAccessToken(request.getLoginId(), 1L),
                        tokenProvider.createRefreshToken(request.getLoginId(), 1L)
                )
        )
                .when(userService)
                .signup(request);

        // when
        UserDto.SignUpResponse response = userService.signup(request);

        // then
        assertThat(tokenProvider.getLoginId(response.getAccessToken())).isEqualTo(request.getLoginId());
        assertThat(tokenProvider.getLoginId(response.getRefreshToken())).isEqualTo(request.getLoginId());
    }

    @DisplayName("회원가입 실패 - 이메일 중복")
    @Test
    void signup_duplicatedEmailExist_fail() {
        // given
        given(userRepository.findByEmail(any())).willReturn(Optional.of(createMockUser(1L)));
        UserDto.SignupRequest request = new UserDto.SignupRequest("김덕성", "dskim@duksung.ac.kr", "dskim", "1234");

        // when & then
        CustomException e = assertThrows(CustomException.class, () -> userService.signup(request));
        assertThat(e.getErrorType()).isEqualTo(EMAIL_ALREADY_EXIST);
    }

    @DisplayName("회원가입 실패 - 아이디 중복")
    @Test
    void signup_duplicateLoginIdExist_fail() {
        // given
        given(userRepository.findByEmail(any())).willReturn(Optional.empty());
        given(userRepository.findByLoginId(any())).willReturn(Optional.of(createMockUser(1L)));
        UserDto.SignupRequest request = new UserDto.SignupRequest("김덕성", "dskim@duksung.ac.kr", "dskim", "1234");

        // when & then
        CustomException e = assertThrows(CustomException.class, () -> userService.signup(request));
        assertThat(e.getErrorType()).isEqualTo(LOGIN_ID_ALREADY_EXIST);
    }
}
