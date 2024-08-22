package com.ajoudev.backend.dto.file.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.core.io.Resource;

@Builder
@Getter
@AllArgsConstructor
public class ProfileImageDownDTO {

    protected Resource image;
    protected String mimeType;

}
