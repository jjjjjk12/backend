package com.ajoudev.backend.dto.member;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserLoginDTO {
    private String id;
    private String password;
}
