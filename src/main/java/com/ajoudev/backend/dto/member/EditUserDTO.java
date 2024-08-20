package com.ajoudev.backend.dto.member;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EditUserDTO {
    @NotBlank(message = "아이디를 입력해주세요")
    @Size(min = 1, max = 32, message = "아이디는 최대 32자까지 가능합니다")
    @Pattern(regexp = "^[a-zA-Z0-9]+$", message = "아이디는 알파벳, 숫자를 조합하여야 합니다")
    private String id;

    @NotBlank(message = "비밀번호를 입력해주세요")
    @Size(min = 10, max = 100, message = "비밀번호는 최소 10자 이상입니다")
    @Pattern(regexp = "^(?=(.*[A-Za-z].*[0-9])|(?=.*[A-Za-z].*[@$!%*?&])|(?=.*[0-9].*[@$!%*?&])).{10,}$", message = "비밀번호는 숫자, 알파벳, 특수문자 중 최소 2가지를 사용해야합니다")
    private String newPassword;

    @NotBlank(message = "닉네임을 입력해주세요")
    @Size(min = 1, max = 32, message = "닉네임은 최대 32자까지 가능합니다")
    @Pattern(regexp = "^[a-zA-Z0-9가-힣@$!%*?&]{1,32}$", message = "닉네임은 숫자, 알파벳, 한글, 특수문자만 가능합니다")
    private String nickname;

    @NotBlank(message = "이메일을 입력해주세요")
    @Email(message = "이메일 형식에 맞지 않습니다")
    private String email;

    @NotBlank(message = "잘못된 비밀번호입니다")
    @Size(min = 10, max = 100, message = "잘못된 비밀번호입니다")
    @Pattern(regexp = "^(?=(.*[A-Za-z].*[0-9])|(?=.*[A-Za-z].*[@$!%*?&])|(?=.*[0-9].*[@$!%*?&])).{10,}$", message = "잘못된 비밀번호입니다")
    private String password;
}
