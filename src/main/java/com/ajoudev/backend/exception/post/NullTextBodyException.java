package com.ajoudev.backend.exception.post;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class NullTextBodyException extends PostingException{
    NullTextBodyException(String gripe) {
        super(gripe);
    }
}
