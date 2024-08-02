package com.ajoudev.backend.repository.member;

import com.ajoudev.backend.entity.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, String> {

    boolean existsByUserid(String userid);

    Optional<Member> findByUserid(String userid);
}
