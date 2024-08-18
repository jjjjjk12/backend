package com.ajoudev.backend.exception.like;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class DuplicatedLikeException extends LikeException{
    DuplicatedLikeException(String gripe) {
        super(gripe);
    }
}
