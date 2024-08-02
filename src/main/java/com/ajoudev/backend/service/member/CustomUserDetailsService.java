package com.ajoudev.backend.service.member;

import com.ajoudev.backend.dto.member.UserDetailsDTO;
import com.ajoudev.backend.entity.member.Member;
import com.ajoudev.backend.repository.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Member member = memberRepository.findByUserid(username).orElse(null);

        if(member == null)
            throw new UsernameNotFoundException("User not found");

        return new UserDetailsDTO(member);


    }
}
