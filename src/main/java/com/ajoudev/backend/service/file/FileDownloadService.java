package com.ajoudev.backend.service.file;


import com.ajoudev.backend.dto.file.response.ProfileImageDownDTO;
import com.ajoudev.backend.entity.file.ProfileImage;
import com.ajoudev.backend.entity.member.Member;
import com.ajoudev.backend.exception.file.NotDownloadableException;
import com.ajoudev.backend.exception.member.NotFoundUserException;
import com.ajoudev.backend.repository.file.ProfileImageRepository;
import com.ajoudev.backend.repository.member.MemberRepository;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class FileDownloadService {

    private final ProfileImageRepository profileImageRepository;
    private final MemberRepository memberRepository;

    public ProfileImageDownDTO profileImageDownload(String id) throws RuntimeException {
        Member member = memberRepository.findByUserid(id).orElse(null);
        if (member == null) {
            throw new NotFoundUserException("ERR_USER_NOT_FOUND");
        }

        ProfileImage profileImage = profileImageRepository.findByUser(member).orElse(null);
        if (profileImage == null) {
            throw new NotDownloadableException("ERR_IMAGE_NOT_FOUND");
        }

        Resource file = new FileSystemResource(profileImage.getFile_path());

        return ProfileImageDownDTO.builder()
                .image(file)
                .mimeType(profileImage.getMime_type())
                .build();

    }
}
