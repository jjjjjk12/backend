package com.ajoudev.backend.dto.comment.response;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Builder
public class MyCommentPageDTO implements CommentDTO{
    private String commentBody;
    private Long commentNum;
    private String user;
    private String id;
    private LocalDateTime commentingDate;
    private String postTitle;
    private Long postNum;

    @QueryProjection
    public MyCommentPageDTO(String commentBody, Long commentNum, String user, String id, LocalDateTime commentingDate, String postTitle, Long postNum) {
        this.commentBody = commentBody;
        this.commentNum = commentNum;
        this.user = user;
        this.id = id;
        this.commentingDate = commentingDate;
        this.postTitle = postTitle;
        this.postNum = postNum;
    }
}
