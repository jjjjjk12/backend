package com.ajoudev.backend.dto.post.response;


import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Builder
public class AnswerPageDTO {
    private Long postNum;
    private String title;
    private String user;
    private String id;
    private LocalDateTime postingDate;
    private Long like;
    private Long dislike;
    private Long comment;
    private Boolean isLiked;
    private Boolean isDisliked;

    @QueryProjection

    public AnswerPageDTO(Long postNum, String title, String user, String id, LocalDateTime postingDate, Long like, Long dislike, Long comment, Boolean isLiked, Boolean isDisliked) {
        this.postNum = postNum;
        this.title = title;
        this.user = user;
        this.id = id;
        this.postingDate = postingDate;
        this.like = like;
        this.dislike = dislike;
        this.comment = comment;
        this.isLiked = isLiked;
        this.isDisliked = isDisliked;
    }
}
