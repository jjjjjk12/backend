package com.ajoudev.backend.exception.file;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class NotUploadableException extends FileException {
    public NotUploadableException(String message) {
        super(message);
    }
}
