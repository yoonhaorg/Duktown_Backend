package com.duktown.global.email;

import com.duktown.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.duktown.global.exception.CustomErrorType.UNABLE_TO_SEND_EMAIL;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MailService {
    private final JavaMailSender javaMailSender;

    // 발신 이메일 데이터 설정
    private SimpleMailMessage createEmailForm(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();

        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);

        return message;
    }

    public void sendEmail(String to, String subject, String text) {
        SimpleMailMessage emailForm = createEmailForm(to, subject, text);
        try {
            javaMailSender.send(emailForm);
        } catch (RuntimeException e) {
            throw new CustomException(UNABLE_TO_SEND_EMAIL);
        }
    }
}
