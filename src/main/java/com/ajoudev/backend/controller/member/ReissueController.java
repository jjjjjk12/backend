package com.ajoudev.backend.controller.member;

import com.ajoudev.backend.jwt.JWTUtil;
import com.ajoudev.backend.service.member.TokenService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
public class ReissueController {

    private final JWTUtil jwtUtil;
    private final TokenService tokenService;

    @PostMapping("/api/reissue")
    public ResponseEntity<Object> reissueToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String refresh = request.getHeader("X-Refresh-Token");
        if (!tokenService.validateRefreshToken(response,refresh)) {
            return ResponseEntity.badRequest().build();
        }

        tokenService.deleteRefreshToken(refresh);
        tokenService.issueToken(response, jwtUtil.getId(refresh));
        return ResponseEntity.ok().build();
    }
}
