package com.ajoudev.backend.config;

import com.ajoudev.backend.jwt.JWTFilter;
import com.ajoudev.backend.jwt.JWTUtil;
import com.ajoudev.backend.jwt.LoginFilter;
import com.ajoudev.backend.jwt.LogoutFilter;
import com.ajoudev.backend.service.member.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final AuthenticationConfiguration configuration;
    private final TokenService tokenService;
    private final JWTUtil jwtUtil;

    @Bean
    public AuthenticationManager manager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public BCryptPasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity security) throws Exception {

        security.csrf(auth -> auth.disable());
        security.formLogin(auth -> auth.disable());
        security.httpBasic(auth -> auth.disable());
        security.authorizeHttpRequests(auth -> auth
                .requestMatchers("/login", "/register", "/reissue").permitAll()
                .anyRequest().authenticated());
        security.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        security.addFilterAt(new LoginFilter(manager(configuration), tokenService), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(new JWTFilter(jwtUtil), UsernamePasswordAuthenticationFilter.class);
        security.addFilterBefore(new LogoutFilter(tokenService), org.springframework.security.web.authentication.logout.LogoutFilter.class);

        return security.build();
    }
}
