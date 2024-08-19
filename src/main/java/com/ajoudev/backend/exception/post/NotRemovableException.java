package com.ajoudev.backend.exception.post;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class NotRemovableException extends PostingException{
    public NotRemovableException(String gripe) {
        super(gripe);
    }
}
