package com.ajoudev.backend.exception.comment;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class CommentNotFoundException extends CommentingException{
    CommentNotFoundException(String gripe) {
        super(gripe);
    }
}
