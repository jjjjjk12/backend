package com.ajoudev.backend.service.file;

import com.ajoudev.backend.entity.file.ProfileImage;
import com.ajoudev.backend.entity.member.Member;
import com.ajoudev.backend.exception.file.FileException;
import com.ajoudev.backend.exception.file.NotUploadableException;
import com.ajoudev.backend.exception.member.NotFoundUserException;
import com.ajoudev.backend.repository.file.ProfileImageRepository;
import com.ajoudev.backend.repository.member.MemberRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

@Service
@Transactional
@RequiredArgsConstructor
public class FileUploadService {

    private final ProfileImageRepository profileImageRepository;
    private final MemberRepository memberRepository;

    public void profileUpload(MultipartFile file) throws RuntimeException, IOException {
        String id = SecurityContextHolder.getContext().getAuthentication().getName();
        Member member = memberRepository.findByUserid(id).orElse(null);

        if (member == null) throw new NotFoundUserException("ERR_USER_NOT_FOUND");
        if (!validateProfileImage(file)) {
            throw new NotUploadableException("ERR_INVALID_PROFILE_IMAGE");
        }

        ProfileImage profileImage = profileImageRepository.findByUser(member).orElse(null);

        if (profileImage == null) {
            profileImage = new ProfileImage();
        }

        String newFileName = member.getUserid() + "_profile." + file.getContentType().substring(6);
        String path = System.getProperty("user.home") + "/" + newFileName;

        profileImage.create(
                newFileName
                ,file.getOriginalFilename()
                ,path
                ,file.getContentType()
                ,member
        );

        File uploadFile = new File(path);
        if (!uploadFile.exists()) {
            uploadFile.mkdirs();
        }

        file.transferTo(uploadFile);
        profileImageRepository.save(profileImage);
    }

    private boolean validateProfileImage(MultipartFile file) {
        if(file.isEmpty()) {
            return false;
        }

        if (file.getSize() >= 5 * 1024 * 1024) {
            return false;
        }

        if (!Objects.equals(file.getContentType(), "image/jpeg")
                && !Objects.equals(file.getContentType(), "image/png")
                && !Objects.equals(file.getContentType(), "image/gif")) {
            return false;
        }

        return true;
    }

}
