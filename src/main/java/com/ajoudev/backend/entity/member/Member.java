package com.ajoudev.backend.entity.member;


import com.ajoudev.backend.dto.member.UserDTO;
import com.ajoudev.backend.dto.member.UserRegistrationDTO;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.nio.charset.StandardCharsets;
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

    public UserDTO toUserDTO() {
        return UserDTO.builder()
                .id(getUserid())
                .email(getEmail())
                .nickname(getNickname())
                .joiningDate(getJoiningDate().toString())
                .build();
    }

    public void changInfo(UserRegistrationDTO userDTO, BCryptPasswordEncoder encoder) {
        if (userDTO.getId() != null) userid = userDTO.getId();
        if (userDTO.getPassword() != null) password = encoder.encode(userDTO.getPassword());
        if (userDTO.getNickname() != null) nickname = userDTO.getNickname();
        if (userDTO.getEmail() != null) email = userDTO.getEmail();
    }

}
