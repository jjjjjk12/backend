package com.ajoudev.backend.dto.comment.response;

import com.querydsl.core.annotations.QueryProjection;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Builder
public class CommentPageDTO {
    private String commentBody;
    private Long commentNum;
    private Long parent;
    private String user;
    private String id;
    private LocalDateTime commentingDate;

    @QueryProjection
    public CommentPageDTO(String commentBody, Long commentNum, Long parent, String user,
                   String id, LocalDateTime commentingDate) {
        this.commentBody = commentBody;
        this.commentNum = commentNum;
        this.parent = parent;
        this.user = user;
        this.id = id;
        this.commentingDate = commentingDate;

    }
}
