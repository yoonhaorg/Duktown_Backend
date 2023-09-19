package com.duktown.global.security.filter;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

// 인증 필터 : HTTP Request를 낚아챔
// 요청의 username과 password를 이용해 토큰 생성
// 토큰을 AuthenticationManager가 받아 AuthenticationProvider에게 넘겨준다
@RequiredArgsConstructor
public class CustomAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws AuthenticationException {
        String email = httpServletRequest.getParameter("email");
        String password = httpServletRequest.getParameter("password");

        Authentication authentication = new UsernamePasswordAuthenticationToken(email, password);

        return authenticationManager.authenticate(authentication);
    }
}
