package com.ajoudev.backend.exception.member;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class MemberException extends RuntimeException{
    public MemberException(String message) {
        super(message);
    }
}
