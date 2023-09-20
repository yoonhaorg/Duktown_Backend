package com.duktown.global.security;

import com.duktown.global.security.filter.CustomAuthenticationFilter;
import com.duktown.global.security.filter.JwtAuthenticationFilter;
import com.duktown.global.security.handler.JwtAccessDeniedHandler;
import com.duktown.global.security.handler.JwtAuthenticationEntryPoint;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.firewall.DefaultHttpFirewall;
import org.springframework.security.web.firewall.HttpFirewall;

import java.util.Arrays;

import static org.springframework.security.config.http.SessionCreationPolicy.*;

@RequiredArgsConstructor
@EnableWebSecurity
@Configuration
public class SecurityConfig {

    private final AuthenticationProvider authenticationProvider;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final AuthenticationSuccessHandler authenticationSuccessHandler;
    private final AuthenticationFailureHandler authenticationFailureHandler;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    // TODO: 인증에 예외를 둘 로직 추가
    private static final String[] AUTH_WHITE_LIST = {
            "/auth/signup",
            "/auth/login"
    };

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        CustomAuthenticationFilter authenticationFilter =
                new CustomAuthenticationFilter(authenticationManagerBuilder.getOrBuild());

        authenticationFilter.setFilterProcessesUrl("/auth/login");
        authenticationFilter.setAuthenticationSuccessHandler(authenticationSuccessHandler);
        authenticationFilter.setAuthenticationFailureHandler(authenticationFailureHandler);

        http.exceptionHandling()
                .accessDeniedHandler(jwtAccessDeniedHandler)
                .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                .and()
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(STATELESS)   // 세션 사용 x
                .and()
                .addFilter(authenticationFilter)
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .authenticationProvider(authenticationProvider)
                .authorizeHttpRequests((auth) -> {
                    Arrays.stream(AUTH_WHITE_LIST)
                            .forEach(authWhiteListElem -> auth.mvcMatchers(authWhiteListElem).permitAll());
                    auth.anyRequest().authenticated();
                });

        return http.build();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return web -> {
            web.httpFirewall(defaultHttpFirewall());
        };
    }

    @Bean
    public HttpFirewall defaultHttpFirewall() {
        return new DefaultHttpFirewall();
    }
}
