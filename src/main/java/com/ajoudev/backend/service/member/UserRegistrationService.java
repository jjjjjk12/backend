package com.ajoudev.backend.service.member;

import com.ajoudev.backend.dto.member.RegistrationMessageDTO;
import com.ajoudev.backend.dto.member.UserDTO;
import com.ajoudev.backend.dto.member.UserIDDTO;
import com.ajoudev.backend.dto.member.UserRegistrationDTO;
import com.ajoudev.backend.entity.member.Member;
import com.ajoudev.backend.repository.member.MemberRepository;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class UserRegistrationService {

    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder encoder;

    public RegistrationMessageDTO registerMember(UserRegistrationDTO userRegistrationDTO) {

        if (memberRepository.existsByUserid(userRegistrationDTO.getId()) || userRegistrationDTO.getId().equals("anonymousUser")) {
            return RegistrationMessageDTO.builder().status("error").message("중복된 아이디입니다").build();
        }
        for (ConstraintViolation<UserRegistrationDTO> violation : Validation.buildDefaultValidatorFactory().getValidator().validate(userRegistrationDTO)) {
            return RegistrationMessageDTO.builder().status("error").message(violation.getMessage()).build();
        }

        Member member = Member.builder()
                .userid(userRegistrationDTO.getId())
                .nickname(userRegistrationDTO.getNickname())
                .password(encoder.encode(userRegistrationDTO.getPassword()))
                .email(userRegistrationDTO.getEmail())
                .joiningDate(ZonedDateTime.now(ZoneId.of("Asia/Seoul")).toLocalDateTime())
                .uuid(createUUID())
                .build();

        memberRepository.save(member);

        UserDTO user = UserDTO.builder()
                .id(member.getUserid())
                .nickname(member.getNickname())
                .email(member.getEmail())
                .joiningDate(member.getJoiningDate().toString())
                .build();

        return RegistrationMessageDTO.builder().status("success").user(user).build();
    }

    public boolean validateDuplicateID(UserIDDTO user) {
        return memberRepository.existsByUserid(user.getId());
    }


    private String createUUID() {
        String uuid;
        do {
            uuid = UUID.randomUUID().toString();
        }while (memberRepository.existsById(uuid));

        return uuid;
    }
}
