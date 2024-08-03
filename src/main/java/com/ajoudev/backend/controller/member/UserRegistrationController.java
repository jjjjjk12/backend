package com.ajoudev.backend.controller.member;

import com.ajoudev.backend.dto.member.RegistrationMessageDTO;
import com.ajoudev.backend.dto.member.UserRegistrationDTO;
import com.ajoudev.backend.service.member.UserRegistrationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserRegistrationController {

    private final UserRegistrationService userRegistrationService;

    @PostMapping("/register")
    public ResponseEntity<Object> userRegister(@RequestBody UserRegistrationDTO userRegistrationDTO) {
        RegistrationMessageDTO messageDTO = userRegistrationService.registerMember(userRegistrationDTO);

        if (messageDTO.getStats().equals("error")) {
            return ResponseEntity.status(401).body(messageDTO);
        }

        if (messageDTO.getStats().equals("success")) {
            return ResponseEntity.ok(messageDTO);
        }

        return ResponseEntity.badRequest().build();
    }

}
