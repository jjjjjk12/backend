package com.ajoudev.backend.exception.post;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class NotEditableException extends PostingException{
    NotEditableException(String gripe) {
        super(gripe);
    }
}
