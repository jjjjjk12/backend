package com.ajoudev.backend.config;

import com.ajoudev.backend.jwt.*;
import com.ajoudev.backend.repository.member.MemberRepository;
import com.ajoudev.backend.service.member.TokenService;
import jakarta.servlet.http.HttpServletRequest;
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
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Collections;
import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final AuthenticationConfiguration configuration;
    private final TokenService tokenService;
    private final MemberRepository memberRepository;
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
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOriginPatterns(Collections.singletonList("*"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH", "FETCH"));
        configuration.setAllowCredentials(true);
        configuration.setAllowedHeaders(Collections.singletonList("*"));
        configuration.setMaxAge(3600L);
        configuration.setExposedHeaders(List.of("Authorization", "X-Refresh-Token", "Access-Control-Allow-Origin"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity security) throws Exception {

        String[] exceptURL = {
                "/api/login"
                ,"/api/register"
                ,"/api/reissue"
                ,"/api/validateID"
                ,"/api/logout"
                ,"/api/normal/list"
                ,"/api/normal"
                ,"/api/comment/list"
        };

        security.csrf(auth -> auth.disable());
        security.formLogin(auth -> auth.disable());
        security.httpBasic(auth -> auth.disable());
        security.authorizeHttpRequests(auth -> auth
                .requestMatchers(exceptURL).permitAll()
                .anyRequest().authenticated());
        security.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        security.addFilterAt(new LoginFilter(manager(configuration), tokenService, memberRepository), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(new JWTFilter(jwtUtil, exceptURL), UsernamePasswordAuthenticationFilter.class);
        security.addFilterBefore(new LogoutFilter(tokenService), org.springframework.security.web.authentication.logout.LogoutFilter.class);
        security.cors((corsCustomizer -> corsCustomizer.configurationSource(corsConfigurationSource())));
        //security.addFilterAt(new MyCorsFilter(corsConfigurationSource()), CorsFilter.class);
        //security.cors(cors -> cors.disable());

        return security.build();
    }
}
