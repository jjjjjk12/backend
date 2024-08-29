package com.ajoudev.backend.exception.post;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class PostNotFoundException extends PostingException {
    public PostNotFoundException(String gripe) {
        super(gripe);
    }
}
