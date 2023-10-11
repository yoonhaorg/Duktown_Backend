package com.duktown.global.security.provider;

import com.duktown.domain.user.entity.User;
import com.duktown.domain.user.entity.UserRepository;
import com.duktown.global.exception.CustomException;
import com.duktown.global.security.service.CustomUserDetails;
import com.duktown.global.type.RoleType;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

import static com.duktown.global.exception.CustomErrorType.*;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Slf4j
@Component
@PropertySource("classpath:application.yml")
@RequiredArgsConstructor
public class JwtTokenProvider {

    // Expiration Time
    public static final long MINUTE = 1000 * 60;
    public static final long HOUR = 60 * MINUTE;
    public static final long DAY = 24 * HOUR;
    public static final long MONTH = 30 * DAY;

    public static final long ACCESS_TOKEN_EXP_TIME = 12 * HOUR;
    public static final long REFRESH_TOKEN_EXP_TIME = 14 * DAY;

    // JWT Secret Key
    @Value("${custom.jwt.secret-key}")
    private String secretKey;
    public Key jwtSecretKey;

    // Header
    public static final String TOKEN_HEADER_PREFIX = "Bearer ";

    // Claim
    public static final String CLAIM_ID = "id";
    public static final String CLAIM_ROLE = "role";

    private final UserRepository userRepository;

    @PostConstruct
    protected void init() {
        this.jwtSecretKey = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
    }

    // TODO: 추후 Delete 필드 추가 시 사용자 조회 메서드 교체
    // Access Token에 해당하는 사용자에 대한 Authentication 객체 반환 메서드
    public Authentication getAuthentication(String accessToken) {
        String loginId = getLoginId(accessToken);
        Long userId = getUserId(accessToken);
        User user = userRepository.findByIdAndLoginId(userId, loginId)
                .orElseThrow(() -> new CustomException(USER_NOT_FOUND));
        UserDetails userDetails = new CustomUserDetails(user);
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    // HttpServletRequest Header의 Token값 반환 메서드
    public String getToken(HttpServletRequest httpServletRequest) {
        String authorizationHeader = httpServletRequest.getHeader(AUTHORIZATION);
        if (authorizationHeader == null || !authorizationHeader.startsWith(TOKEN_HEADER_PREFIX)) {
            return null;
        }
        return authorizationHeader.substring(TOKEN_HEADER_PREFIX.length());
    }

    // Access Token 생성 메서드
    public String createAccessToken(String loginId, Long userId, RoleType roleType) {
        return Jwts.builder()
                .setHeaderParam("typ", "JWT")
                .setSubject(loginId)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + ACCESS_TOKEN_EXP_TIME))
                .claim(CLAIM_ID, userId)
                .claim(CLAIM_ROLE, roleType.getKey())
                .signWith(jwtSecretKey, SignatureAlgorithm.HS256)
                .compact();
    }

    // Refresh Token 생성 메서드
    public String createRefreshToken(String loginId, Long userId, RoleType roleType) {
        return Jwts.builder()
                .setHeaderParam("typ", "JWT")
                .setSubject(loginId)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + REFRESH_TOKEN_EXP_TIME))
                .claim(CLAIM_ID, userId)
                .claim(CLAIM_ROLE, roleType.getKey())
                .signWith(jwtSecretKey, SignatureAlgorithm.HS256)
                .compact();
    }

    // Token에서 Claim 추출
    public Claims getClaim(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(jwtSecretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // Token에서 사용자 email 추출 메서드
    public String getLoginId(String token) {
        return getClaim(token).getSubject();
    }

    // Token에서 사용자 ID 추출 메서드
    public Long getUserId(String token) {
        return (Long) getClaim(token).get(CLAIM_ID);
    }

    // Token에서 사용자 RoleType 추출 메서드
    public RoleType getRoleType(String token) {
        return (RoleType) getClaim(token).get(CLAIM_ROLE);
    }

    // Token 유효성 검증 메서드
    // TODO: ExceptionHandling 수정
    public void validateToken(String token) {
        Jwts.parserBuilder()
                .setSigningKey(jwtSecretKey)
                .build()
                .parseClaimsJws(token);
        try {
            Jwts.parserBuilder()
                    .setSigningKey(jwtSecretKey)
                    .build()
                    .parseClaimsJws(token);
        } catch (SignatureException | IllegalArgumentException | UnsupportedJwtException | MalformedJwtException e) {
            throw new CustomException(INVALID_TOKEN);
        } catch (ExpiredJwtException e) {
            throw new CustomException(ACCESS_TOKEN_EXPIRED);
        } catch (Exception e) {
            throw e;
        }
    }
}
