package com.ajoudev.backend.exception.comment;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class NullCommentBodyException extends CommentingException{
    NullCommentBodyException(String gripe) {
        super(gripe);
    }
}
