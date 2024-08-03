package com.ajoudev.backend.repository.member;

import com.ajoudev.backend.entity.member.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, String> {

    void deleteByRefresh(String refresh);

    boolean existsByRefresh(String refresh);
}
