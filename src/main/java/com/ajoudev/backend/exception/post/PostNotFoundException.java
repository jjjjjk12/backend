package com.ajoudev.backend.exception.post;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class PostNotFoundException extends PostingException {
    PostNotFoundException(String gripe) {
        super(gripe);
    }
}
