package com.duktown.global.security.filter;

import com.duktown.global.exception.CustomException;
import com.duktown.global.security.dto.LoginRequestDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.duktown.global.exception.CustomErrorType.SERVER_INTERNAL_ERROR;

// 인증 필터 : HTTP Request를 낚아챔
// 요청의 username과 password를 이용해 토큰 생성
// 토큰을 AuthenticationManager가 받아 AuthenticationProvider에게 넘겨준다
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws AuthenticationException {
        ObjectMapper objectMapper = new ObjectMapper();
        LoginRequestDto loginRequestDto;
        try {
            loginRequestDto = objectMapper.readValue(httpServletRequest.getInputStream(), LoginRequestDto.class);
        } catch (Exception e) {
            throw new CustomException(SERVER_INTERNAL_ERROR);   // TODO: CustomException ErrorResponse 처리
        }

        Authentication authentication = new UsernamePasswordAuthenticationToken(
                loginRequestDto.getLoginId(),
                loginRequestDto.getPassword()
        );

        return authenticationManager.authenticate(authentication);
    }
}
