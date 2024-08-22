package com.ajoudev.backend.repository.file;

import com.ajoudev.backend.entity.file.ProfileImage;
import com.ajoudev.backend.entity.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProfileImageRepository extends JpaRepository<ProfileImage, Long> {

    Optional<ProfileImage> findByUser(Member user);

}
