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
    private String id;
    private String user;
    private Long like;
    private Long dislike;
    private LocalDateTime postingDate;
    private boolean isLiked;
    private boolean isDisliked;
    private boolean isAdopted;

    @QueryProjection
    public ViewAnswerDTO(Long postNum, String title, String textBody, String id, String user, Long like, Long dislike, LocalDateTime postingDate, boolean isLiked, boolean isDisliked, boolean isAdopted) {
        this.postNum = postNum;
        this.title = title;
        this.textBody = textBody;
        this.id = id;
        this.user = user;
        this.like = like;
        this.dislike = dislike;
        this.postingDate = postingDate;
        this.isLiked = isLiked;
        this.isDisliked = isDisliked;
        this.isAdopted = isAdopted;
    }
}
