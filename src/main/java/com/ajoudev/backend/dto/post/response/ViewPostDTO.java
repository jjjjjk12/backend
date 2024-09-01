package com.ajoudev.backend.dto.post.response;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Builder
public class ViewPostDTO {
    private Long postNum;
    private String title;
    private String textBody;
    private String id;
    private String user;
    private Long like;
    private Long visit;
    private Long comment;
    private LocalDateTime postingDate;
    private boolean isLiked;

    @QueryProjection
    public ViewPostDTO(Long postNum, String title, String textBody, String id, String user, Long like, Long visit, Long comment, LocalDateTime postingDate, boolean isLiked) {
        this.postNum = postNum;
        this.title = title;
        this.textBody = textBody;
        this.id = id;
        this.user = user;
        this.like = like;
        this.visit = visit;
        this.comment = comment;
        this.postingDate = postingDate;
        this.isLiked = isLiked;
    }
}
