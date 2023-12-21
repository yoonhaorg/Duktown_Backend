package com.duktown.domain.user.service;

import com.duktown.domain.emailCert.dto.EmailCertDto;
import com.duktown.domain.user.dto.UserDto;
import com.duktown.domain.user.entity.User;
import com.duktown.domain.user.entity.UserRepository;
import com.duktown.global.email.MailService;
import com.duktown.global.exception.CustomException;
import com.duktown.global.security.provider.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.util.Date;

import static com.duktown.global.exception.CustomErrorType.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;
    private final MailService mailService;

    // 아이디 중복 체크 메서드
    public UserDto.IdCheckResponse idCheck(UserDto.IdCheckRequest idCheckRequest) {
        User user = userRepository.findByLoginId(idCheckRequest.getLoginId())
                .orElse(null);

        if (user != null) {
            return new UserDto.IdCheckResponse(true);
        }
        return new UserDto.IdCheckResponse(false);
    }

    // 사용자 회원가입 메서드
    @Transactional
    public UserDto.SignUpResponse signup(UserDto.SignupRequest signupRequest) {

        /*
        * 회원가입 이전 과정이 이메일, 아이디 중복 체크이지만,
        * 전 과정 없이 이 요청만 단독으로 들어오는 경우를 예방하기 위해 signup 요청때도 다시 한번 중복 체크를 수행한다.
        */
        // 이메일 중복 체크
        emailDuplicateCheck(signupRequest.getEmail());

        // 아이디 중복 체크
        idDuplicateCheck(signupRequest.getLoginId());

        // 이메일 인증 여부 체크
//        EmailCert emailCert = emailCertRepository.findByEmail(signupRequest.getEmail()).orElseThrow(
//                () -> new CustomException(CustomErrorType.EMAIL_CERT_NOT_FOUND)
//        );
//
//        if (!emailCert.getCertified()) {
//            throw new CustomException(CustomErrorType.EMAIL_CERT_FAILED);
//        }

        // 비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(signupRequest.getPassword());

        // 사용자 등록
        User user = signupRequest.toEntity(encodedPassword);
        userRepository.save(user);

        String accessToken = jwtTokenProvider.createAccessToken(user.getLoginId(), user.getId(), user.getRoleType());
        String refreshToken = jwtTokenProvider.createRefreshToken(user.getLoginId(), user.getId(), user.getRoleType());
        user.updateRefreshToken(refreshToken);
        return new UserDto.SignUpResponse(accessToken, refreshToken);
    }

    // 비밀번호 찾기 - 이메일로 계정 존재여부 확인
    public void userEmailExists(EmailCertDto.EmailRequest request) {
        userRepository.findByEmail(request.getEmail()).orElseThrow(() -> new CustomException(USER_NOT_FOUND));
    }

    @Transactional
    // 임시 비밀번호 전송
    public void temporaryPwdEmailSend(UserDto.EmailRequest request) {
        User user = userRepository.findByEmail(request.getEmail()).orElseThrow(() -> new CustomException(USER_NOT_FOUND));

        // 임시 비밀번호 생성
        String temporaryPwd = createTemporaryPwd();

        // 사용자 비밀번호 업데이트
        user.updatePassword(passwordEncoder.encode(temporaryPwd));

        String subject = "[덕타운] 임시 비밀번호 발급 안내";
        String text = "임시 비밀번호를 발급했습니다.\n\n" +
                "임시 비밀번호: " + temporaryPwd + "\n\n" +
                "해당 비밀번호로 로그인 후 반드시 비밀번호를 재설정해 주시기 바랍니다.\n" +
                "감사합니다.\n\n" +
                "-덕타운 운영팀";

        mailService.sendAsyncEmail(request.getEmail(), subject, text);
    }

    private String createTemporaryPwd() {
        final String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz012345678!@#$%^&*-_=+";

        StringBuilder sb = new StringBuilder();
        SecureRandom sr = new SecureRandom();
        sr.setSeed(new Date().getTime());

        for (int i = 0; i < 10; i++) {
            sb.append(chars.charAt(sr.nextInt(chars.length())));
        }

        return sb.toString();
    }

    @Transactional
    public void logout(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new CustomException(USER_NOT_FOUND));
        user.deleteRefreshToken();
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
