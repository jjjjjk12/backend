package com.ajoudev.backend.exception.post;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class PostingException extends RuntimeException{
    PostingException(String gripe) {
        super(gripe);
    }
}
