package com.ajoudev.backend.exception.like;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class LikeException extends RuntimeException{
    LikeException(String gripe) {
        super(gripe);
    }
}
