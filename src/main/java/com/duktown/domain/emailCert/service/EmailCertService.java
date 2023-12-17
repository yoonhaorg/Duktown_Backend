package com.duktown.domain.emailCert.service;

import com.duktown.domain.emailCert.dto.EmailCertDto;
import com.duktown.domain.emailCert.entity.EmailCert;
import com.duktown.domain.emailCert.entity.EmailCertRepository;
import com.duktown.domain.user.entity.User;
import com.duktown.domain.user.entity.UserRepository;
import com.duktown.global.email.MailService;
import com.duktown.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Random;

import static com.duktown.global.exception.CustomErrorType.*;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EmailCertService {
    private final UserRepository userRepository;
    private final EmailCertRepository emailCertRepository;
    private final MailService mailService;

    // 이메일로 인증번호 발송
    @Transactional
    public EmailCertDto.EmailResponse emailSend(EmailCertDto.EmailRequest request) {

        // 이미 가입된 이메일이면, isDuplicated: true 응답
        User user = userRepository.findByEmail(request.getEmail())
                .orElse(null);

        if (user != null) {
            return new EmailCertDto.EmailResponse(true);
        }

        String certCode = createCertNumber();
        String subject = "[덕타운] 이메일 인증코드 발송";
        String text = "덕타운 서비스 이용을 위해 덕성 이메일 인증이 필요합니다.\n" +
                "아래의 이메일 인증 코드를 확인 후 정확히 입력해주세요.\n\n" +
                certCode + "\n\n" +
                "코드는 10분 후 만료됩니다.\n" +
                "감사합니다.\n\n" +
                "-덕타운 운영팀";

        sendCertEmail(request.getEmail(), certCode, subject, text);

        // 중복 x => 이메일 발송 후 isDuplicated : false 응답
        return new EmailCertDto.EmailResponse(false);
    }

    // 이메일 인증
    public void emailCert(EmailCertDto.CertRequest request) {
        // 인증 요청 내역 조회 -> 인증요청을 한 적이 없거나, 10분 이상 지나면 조회 x
        EmailCert emailCert = emailCertRepository.findByEmail(request.getEmail()).orElseThrow(
                () -> new CustomException(EMAIL_CERT_NOT_FOUND)
        );

        // 입력한 코드와 인증코드가 같을 때
        if (emailCert.getCertCode().equals(request.getCertCode())) {

            // 인증 여부가 참일 때 이미 가입한 이메일인지 체크
            if (emailCert.getCertified()) {
                User user = userRepository.findByEmail(request.getEmail()).orElse(null);

                if (user != null) {
                    throw new CustomException(EMAIL_ALREADY_EXIST);
                }
            }

            // 이메일 인증 성공
            emailCert.updateCertified(true);

        } else {
            // 일치하지 않으면 인증 실패
            throw new CustomException(EMAIL_CERT_FAILED);
        }

    }

    // 아이디 찾기 이메일 발송
    @Transactional
    public void idFindEmailSend(EmailCertDto.EmailRequest request) {
        // 해당 이메일로 가입한 회원이 있는지 조회
        userExists(request.getEmail());

        String certCode = createCertNumber();
        String subject = "[덕타운] 아이디 찾기 인증코드 발송";
        String text = "아이디 찾기를 위한 코드를 발송드립니다.\n" +
                "아래의 인증 코드를 확인 후 정확히 입력해주세요.\n\n" +
                certCode + "\n\n" +
                "코드는 10분 후 만료됩니다.\n" +
                "감사합니다.\n\n" +
                "-덕타운 운영팀";

        sendCertEmail(request.getEmail(), certCode, subject, text);
    }

    // 아이디 찾기
    public EmailCertDto.LoginIdResponse idFind(EmailCertDto.CertRequest request) {
        EmailCert emailCert = emailCertRepository.findByEmail(request.getEmail()).orElseThrow(() -> new CustomException(EMAIL_CERT_NOT_FOUND));

        // 인증 번호가 같지 않으면 에러
        if (!emailCert.getCertCode().equals(request.getCertCode())) {
            throw new CustomException(EMAIL_CERT_FAILED);
        }

        // 같으면 회원 찾아서 로그인 아이디 응답
        User user = userRepository.findByEmail(request.getEmail()).orElseThrow(() -> new CustomException(USER_NOT_FOUND));
        return new EmailCertDto.LoginIdResponse(user.getLoginId());
    }

    // 인증번호 생성 메서드
    private String createCertNumber() {
        Random random = new Random();
        random.setSeed(System.currentTimeMillis());
        StringBuilder certNumber = new StringBuilder();


        for (int i = 0; i < 6; i++) {
            certNumber.append(random.nextInt(9));
        }

        return certNumber.toString();
    }

    private void sendCertEmail(String email, String certCode, String subject, String text) {
        EmailCert emailCert = emailCertRepository.findByEmail(email).orElse(null);

        // 기존 인증요청이 있었다면 인증번호만 업데이트
        if (emailCert != null) {
            emailCert.updateCertCode(certCode);
        }
        else {
            EmailCertDto.CertRequest certRequest = new EmailCertDto.CertRequest(email, certCode);
            emailCertRepository.save(certRequest.toEntity());
        }

        // 비동기로 메일 전송 -> 응답을 빨리 보내 인증번호 입력 창으로 넘어가야 하기 때문
        mailService.sendAsyncEmail(email, subject, text);
    }

    private void userExists(String email) {
        userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(USER_NOT_FOUND));
    }
}
