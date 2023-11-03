package com.duktown.global.security.handler;

import com.duktown.domain.user.dto.UserDto;
import com.duktown.domain.user.entity.User;
import com.duktown.domain.user.entity.UserRepository;
import com.duktown.global.security.service.CustomUserDetails;
import com.duktown.global.security.provider.JwtTokenProvider;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Component
@RequiredArgsConstructor
public class UserAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;

    @Transactional
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

        User user = userRepository.findByLoginId(userDetails.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("가입된 아이디가 존재하지 않습니다."));

        // JWT Token 생성, 응답
        String accessToken = jwtTokenProvider.createAccessToken(user.getLoginId(), user.getId());
        String refreshToken = jwtTokenProvider.createRefreshToken(user.getLoginId(), user.getId());

        user.updateRefreshToken(refreshToken);
        response.setContentType(APPLICATION_JSON_VALUE);

        new ObjectMapper().writeValue(response.getWriter(),
                new UserDto.UserTokenResponse(user.getRoleType(), accessToken, refreshToken));
    }
}
