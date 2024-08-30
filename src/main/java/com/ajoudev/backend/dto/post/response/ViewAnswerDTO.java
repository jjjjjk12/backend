package com.ajoudev.backend.dto.post.response;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Data
@NoArgsConstructor
@Builder
public class ViewAnswerDTO {
    private Long postNum;
    private String title;
    private String textBody;
    private String user;
    private String id;
    private LocalDateTime postingDate;
    private Long like;
    private Long dislike;
    private Long comment;
    private Boolean isLiked;
    private Boolean isDisliked;
    private Boolean isAdopted;

    @QueryProjection
    public ViewAnswerDTO(Long postNum, String title, String textBody, String user, String id, LocalDateTime postingDate, Long like, Long dislike, Long comment, Boolean isLiked, Boolean isDisliked, Boolean isAdopted) {
        this.postNum = postNum;
        this.title = title;
        this.textBody = textBody;
        this.user = user;
        this.id = id;
        this.postingDate = postingDate;
        this.like = like;
        this.dislike = dislike;
        this.comment = comment;
        this.isLiked = isLiked;
        this.isDisliked = isDisliked;
        this.isAdopted = isAdopted;
    }
}
