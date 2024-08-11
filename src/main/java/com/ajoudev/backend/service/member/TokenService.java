package com.ajoudev.backend.service.member;

import com.ajoudev.backend.entity.member.RefreshToken;
import com.ajoudev.backend.jwt.JWTUtil;
import com.ajoudev.backend.repository.member.RefreshTokenRepository;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

@Service
@Transactional
@RequiredArgsConstructor
public class TokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final JWTUtil jwtUtil;

    public void issueToken(HttpServletResponse response, String id) {

        String access = jwtUtil.createJwt(id, "access", 1000 * 60 * 10L);
        String refresh = jwtUtil.createJwt(id, "refresh", 1000 * 60 * 60 * 24L);

        response.addHeader("Authorization", "Bearer " + access);
        response.addHeader("X-Refresh-Token", refresh);

        RefreshToken token = RefreshToken.builder()
                .expiration(jwtUtil.getExpiration(refresh))
                .refresh(refresh)
                .userid(id).build();


        refreshTokenRepository.save(token);


    }

    public boolean validateRefreshToken(HttpServletResponse response ,String refresh) throws IOException {
        if (refresh == null) {
            response.getWriter().write("{ \"message\": \"REFRESH 토큰이 없습니다\" }");
            return false;
        }
        if (!refreshTokenRepository.existsByRefresh(refresh)) {
            response.getWriter().write("{ \"message\": \"존재하지 않는 REFRESH 토큰입니다\" }");
            return false;
        }
        try {
            jwtUtil.isExpired(refresh);
        } catch (Exception e) {
            refreshTokenRepository.deleteByRefresh(refresh);
            response.getWriter().write("{ \"message\": \"REFRESH 토큰이 만료되었습니다\" }");
            return false;
        }
        if (!jwtUtil.getCategory(refresh).equals("refresh")) {
            response.getWriter().write("{ \"message\": \"유효한 REFRESH 토큰이 아닙니다\" }");
            return false;
        }


        return true;
    }

    public void deleteRefreshToken(String refresh) {
        refreshTokenRepository.deleteByRefresh(refresh);
    }


}
