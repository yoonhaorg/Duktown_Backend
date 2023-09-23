package com.duktown.global.security.service;

import com.duktown.domain.user.entity.User;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

// 스프링 시큐리티에서 사용자의 인증 정보를 담아두는 인터페이스
@Getter
@RequiredArgsConstructor
public class CustomUserDetails implements UserDetails {
    private final User user;

    // 권한 반환
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(user.getRoleType().getKey()));
        return authorities;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    // 사용자 식별 가능한 속성 반환(사용자 로그인 아이디)
    @Override
    public String getUsername() {
        return user.getLoginId();
    }

    // 계정 만료 여부 반환
    @Override
    public boolean isAccountNonExpired() {
        return true;    // 만료 x
    }

    // 계정 잠금 여부 반환
    @Override
    public boolean isAccountNonLocked() {
        return true;   // 잠금 x
    }

    // 패스워드 만료 여부 반환
    @Override
    public boolean isCredentialsNonExpired() {
        return true;   // 만료 x
    }

    // 계정 사용 여부 반환
    @Override
    public boolean isEnabled() {
        return true;   // 사용 가능
    }
}
