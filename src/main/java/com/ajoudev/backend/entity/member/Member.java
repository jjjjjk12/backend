package com.ajoudev.backend.entity.member;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "member")
public class Member {

    @Column(nullable = false)
    private String password;
    @Column(nullable = false)
    private String nickname;
    @Column(nullable = false, unique = true)
    private String userid;
    @Id
    @Column(nullable = false, unique = true)
    private String uuid;
    @Column(nullable = false)
    private String email;
    @Column(nullable = false)
    private final String role = "ROLE_USER";
    @Column(nullable = false)
    private LocalDateTime joiningDate;

}
