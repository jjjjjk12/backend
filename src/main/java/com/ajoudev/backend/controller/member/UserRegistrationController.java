package com.ajoudev.backend.controller.member;

import com.ajoudev.backend.dto.member.RegistrationMessageDTO;
import com.ajoudev.backend.dto.member.UserIDDTO;
import com.ajoudev.backend.dto.member.UserRegistrationDTO;
import com.ajoudev.backend.service.member.UserRegistrationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserRegistrationController {

    private final UserRegistrationService userRegistrationService;

    @PostMapping("/register")
    public ResponseEntity<Object> userRegister(@RequestBody UserRegistrationDTO userRegistrationDTO) {
        RegistrationMessageDTO messageDTO = userRegistrationService.registerMember(userRegistrationDTO);

        if (messageDTO.getStatus().equals("error")) {
            return ResponseEntity.status(401).body(messageDTO);
        }

        if (messageDTO.getStatus().equals("success")) {
            return ResponseEntity.ok(messageDTO);
        }

        return ResponseEntity.badRequest().build();
    }

    @PostMapping("/validateID")
    public ResponseEntity<Object> validateDuple(@RequestBody UserIDDTO user) {

        if (userRegistrationService.validateDuplicateID(user)) {
            return ResponseEntity.status(200).body(RegistrationMessageDTO.builder()
                    .message("중복된 아이디입니다").status("error").build());
        }

        else {
            return ResponseEntity.status(200).body(RegistrationMessageDTO.builder()
                    .message("사용가능한 아이디입니다").status("correct").build());
        }
    }

}
