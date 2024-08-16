package com.ajoudev.backend.jwt;

import com.ajoudev.backend.service.member.TokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;

@RequiredArgsConstructor
public class LogoutFilter extends GenericFilterBean {

    private final TokenService tokenService;
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        doFilter((HttpServletRequest) servletRequest, (HttpServletResponse) servletResponse, filterChain);
    }

    private void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws  IOException, ServletException {
        String uri = request.getRequestURI();
        if (!uri.matches("^\\/api/logout$")) {
            filterChain.doFilter(request, response);
            return;
        }
        if (!request.getMethod().equals("POST")) {
            filterChain.doFilter(request, response);
            return;
        }
        String refresh = request.getHeader("X-Refresh-Token");
        if (!tokenService.validateRefreshToken(response, refresh)) {
            response.setStatus(400);
            return;
        }

        tokenService.deleteRefreshToken(refresh);
        response.setStatus(200);
    }
}
