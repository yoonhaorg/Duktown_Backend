package com.duktown.global.security.filter;

import com.duktown.global.exception.CustomErrorType;
import com.duktown.global.exception.ErrorResponse;
import com.duktown.global.security.provider.JwtTokenProvider;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.duktown.global.exception.CustomErrorType.*;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

// 모든 엔드포인트에 대해 accessToken 확인하는 필터
@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String accessToken = jwtTokenProvider.getToken(request);

        if (accessToken != null) {
            try {
                jwtTokenProvider.validateToken(accessToken);
                Authentication authentication = jwtTokenProvider.getAuthentication(accessToken);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } catch (SignatureException | IllegalArgumentException | UnsupportedJwtException | MalformedJwtException e) {
                handleErrorResponse(response, INVALID_TOKEN);
            } catch (ExpiredJwtException e) {
                handleErrorResponse(response, ACCESS_TOKEN_EXPIRED);
                return;
            } catch (Exception e) {
                handleErrorResponse(response, UNHANDLED_TOKEN_ERROR);
                return;
            }
        }
        filterChain.doFilter(request, response);
    }

    // TODO: getWriter() has already been called for this response 오류 해결
    private void handleErrorResponse(HttpServletResponse response, CustomErrorType errorType) throws IOException {
        if (!response.isCommitted()) {
            response.setStatus(errorType.getStatusCode());
            response.setContentType(APPLICATION_JSON_VALUE);
            response.setCharacterEncoding("utf-8");
            ErrorResponse errorResponse = new ErrorResponse(errorType);
            new ObjectMapper().writeValue(response.getWriter(), errorResponse);
        }
    }
}
