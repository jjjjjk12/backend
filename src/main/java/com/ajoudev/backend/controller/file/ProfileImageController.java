package com.ajoudev.backend.controller.file;

import com.ajoudev.backend.dto.file.response.FileUploadMessageDTO;
import com.ajoudev.backend.dto.file.response.ProfileImageDownDTO;
import com.ajoudev.backend.exception.file.FileException;
import com.ajoudev.backend.service.file.FileDownloadService;
import com.ajoudev.backend.service.file.FileUploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/file/profile")
@RequiredArgsConstructor
public class ProfileImageController {

    private final FileUploadService fileUploadService;
    private final FileDownloadService fileDownloadService;

    @PostMapping("/upload")
    public FileUploadMessageDTO uploadImage(@RequestParam final MultipartFile file) {
        FileUploadMessageDTO messageDTO;

        try {
            fileUploadService.profileUpload(file);
            messageDTO = FileUploadMessageDTO.builder()
                    .status("success").build();
        } catch (IOException e) {
            messageDTO = FileUploadMessageDTO.builder().status("error").message("ERR_FAIL_TO_UPLOAD_IMAGE").build();
        } catch (FileException e) {
            messageDTO = FileUploadMessageDTO.builder().status("error").message(e.getMessage()).build();
        }
        return messageDTO;
    }

    @GetMapping("/download")
    public ResponseEntity<Resource> downloadImage(@RequestParam final String user) {
        try {
            ProfileImageDownDTO downDTO = fileDownloadService.profileImageDownload(user);
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_TYPE, downDTO.getMimeType())
                    .body(downDTO.getImage());
        } catch (FileException e) {
            return ResponseEntity.notFound().build();
        }
    }

}
