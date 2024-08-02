package com.ajoudev.backend.entity.member;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;

@Entity
@Table(name = "refresh_token")
@Data
@Builder
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long num;
    private String expiration;
    private String userid;
    private String refresh;
}
