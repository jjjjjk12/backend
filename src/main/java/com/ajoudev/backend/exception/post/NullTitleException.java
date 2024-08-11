package com.ajoudev.backend.exception.post;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class NullTitleException extends PostingException {
    NullTitleException(String gripe) {
        super(gripe);
    }
}
