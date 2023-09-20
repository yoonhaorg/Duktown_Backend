package com.duktown.global.security.handler;

import com.duktown.global.exception.CustomErrorType;
import com.duktown.global.exception.ErrorResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.duktown.global.exception.CustomErrorType.AUTHENTICATION_REQUIRED;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    // 인증이 필요한 엔드포인트에 대해 인증되지 않았을 때 동작하는 Handler
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        response.setStatus(AUTHENTICATION_REQUIRED.getStatusCode());
        response.setContentType(APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("utf-8");
        new ObjectMapper().writeValue(response.getWriter(),
                new ErrorResponse(AUTHENTICATION_REQUIRED));
    }
}
