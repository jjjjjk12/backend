package com.ajoudev.backend.jwt;

import com.ajoudev.backend.dto.member.UserDetailsDTO;
import com.ajoudev.backend.entity.member.Member;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;

@RequiredArgsConstructor
public class JWTFilter extends OncePerRequestFilter {

    private final JWTUtil jwtUtil;
    private final String[] exceptURL;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String url = request.getRequestURI();
        if(Arrays.asList(exceptURL).contains(url) && !url.equals("/api/normal") && !url.equals("/api/question") && !url.equals("/api/answer")) {
            filterChain.doFilter(request, response);
            return;
        }

        String authorization = request.getHeader("Authorization");

        if (authorization == null) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = authorization.split(" ")[1];

        try {
            jwtUtil.isExpired(token);
        } catch (ExpiredJwtException e) {

            setBody(response, "만료된 ACCESS 토큰입니다");
            response.setStatus(401);
            return;
        }

        if (!jwtUtil.getCategory(token).equals("access")) {
            setBody(response, "유효하지 않은 ACCESS 토큰입니다");
            response.setStatus(401);
            return;
        }

        Member member = Member.builder().userid(jwtUtil.getId(token)).build();
        UserDetailsDTO userDetailsDTO = new UserDetailsDTO(member);


        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetailsDTO, null, userDetailsDTO.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(authentication);
        filterChain.doFilter(request, response);
    }

    private void setBody(HttpServletResponse response,String str) throws IOException {
        response.getWriter().write("{\n" + "\t\"message\": \"" + str + "\"\n}");
    }

}
