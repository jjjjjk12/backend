package com.ajoudev.backend.exception.file;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class FileException extends RuntimeException {
    public FileException(String message) {
        super(message);
    }
}
