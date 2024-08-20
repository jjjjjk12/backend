package com.ajoudev.backend.repository.member;

import com.ajoudev.backend.entity.member.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, String> {

    void deleteByRefresh(String refresh);

    void deleteByExpirationBefore(LocalDateTime expiration);

    boolean existsByRefresh(String refresh);
}
