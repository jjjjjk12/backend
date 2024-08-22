package com.ajoudev.backend.exception.file;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class NotDownloadableException extends FileException {
    public NotDownloadableException(String message) {
        super(message);
    }
}
