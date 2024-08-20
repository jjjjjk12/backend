package com.ajoudev.backend.exception.member;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class NotFoundUserException extends MemberException {
    public NotFoundUserException(String message) {
        super(message);
    }
}
