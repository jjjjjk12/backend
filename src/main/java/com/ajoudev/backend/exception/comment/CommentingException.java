package com.ajoudev.backend.exception.comment;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class CommentingException extends RuntimeException{
    CommentingException(String gripe) {
        super(gripe);
    }
}
