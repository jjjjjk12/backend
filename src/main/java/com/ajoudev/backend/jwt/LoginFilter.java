package com.ajoudev.backend.jwt;

import com.ajoudev.backend.dto.member.RegistrationMessageDTO;
import com.ajoudev.backend.dto.member.UserDTO;
import com.ajoudev.backend.dto.member.UserDetailsDTO;
import com.ajoudev.backend.dto.member.UserLoginDTO;
import com.ajoudev.backend.repository.member.MemberRepository;
import com.ajoudev.backend.service.member.TokenService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;

@RequiredArgsConstructor
public class LoginFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;
    private final MemberRepository memberRepository;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        setUsernameParameter("id");
        UserLoginDTO user;
        try {
            user = (new ObjectMapper()).readValue(request.getInputStream(), UserLoginDTO.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(user.getId(), user.getPassword());

        return authenticationManager.authenticate(token);

    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        UserDetailsDTO userDetailsDTO = (UserDetailsDTO) authResult.getPrincipal();
        String id = userDetailsDTO.getUsername();

        UserDTO user = memberRepository.findByUserid(id).get().toUserDTO();
        RegistrationMessageDTO message = RegistrationMessageDTO.builder()
                .status("success")
                .user(user)
                .build();
        String body = (new ObjectMapper()).writeValueAsString(message);

        tokenService.issueToken(response,id);
        response.setStatus(200);
        response.getWriter().write(body);

    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        super.unsuccessfulAuthentication(request, response, failed);
        response.setStatus(401);
    }
}
