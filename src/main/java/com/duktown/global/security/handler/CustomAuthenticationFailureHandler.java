package com.duktown.global.security.handler;

import com.duktown.global.exception.ErrorResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.duktown.global.exception.CustomErrorType.LOGIN_FAILED;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Component
public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        response.setContentType(APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("utf-8");

        // 특정 인증 예외에 대해 처리하고 싶다면 아래 코드 활용
        // Class<? extends AuthenticationException> exceptionClass = exception.getClass();

        response.setStatus(LOGIN_FAILED.getStatusCode());
        new ObjectMapper().writeValue(response.getWriter(), new ErrorResponse(LOGIN_FAILED));
    }
}
