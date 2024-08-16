package com.ajoudev.backend.dto.comment.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class CommentPageDTO {
    private String commentBody;
    private Long commentNum;
    private String user;
    private LocalDateTime commentingDate;
}
