package com.ajoudev.backend.controller.member;

import com.ajoudev.backend.dto.member.RegistrationMessageDTO;
import com.ajoudev.backend.dto.member.UserDTO;
import com.ajoudev.backend.dto.member.UserRegistrationDTO;
import com.ajoudev.backend.dto.post.response.PostMessageDTO;
import com.ajoudev.backend.exception.post.PostingException;
import com.ajoudev.backend.service.member.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/member")
public class UserController {

    private final UserService userService;

    @PostMapping("/delete")
    public RegistrationMessageDTO delete() {
        RegistrationMessageDTO messageDTO;

        try {
            userService.delete();
            messageDTO = RegistrationMessageDTO.builder()
                    .status("success")
                    .build();
        } catch (RuntimeException e) {
            e.printStackTrace();
            messageDTO = RegistrationMessageDTO.builder()
                    .status("error")
                    .message("회원을 탈퇴할 수 없습니다")
                    .build();
            return messageDTO;
        }

        return messageDTO;
    }

    @PostMapping("/edit")
    public RegistrationMessageDTO edit(UserRegistrationDTO userRegistrationDTO) {
        RegistrationMessageDTO messageDTO;

        try {
            UserDTO userDTO = userService.edit(userRegistrationDTO);
            messageDTO = RegistrationMessageDTO.builder()
                    .status("success")
                    .user(userDTO)
                    .build();
        } catch (RuntimeException e) {
            e.printStackTrace();
            messageDTO = RegistrationMessageDTO.builder()
                    .status("error")
                    .message(e.getMessage())
                    .build();
            return messageDTO;
        }

        return messageDTO;
    }

}
