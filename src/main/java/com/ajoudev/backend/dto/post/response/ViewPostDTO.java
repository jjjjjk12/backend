package com.ajoudev.backend.dto.post.response;

import com.ajoudev.backend.entity.member.Member;
import com.querydsl.core.annotations.QueryProjection;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
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
    private LocalDateTime postingDate;
    private boolean isLiked;

    @QueryProjection
    public ViewPostDTO(Long postNum, String title, String textBody, String id, String user, Long like, Long visit, LocalDateTime postingDate, boolean isLiked) {
        this.postNum = postNum;
        this.title = title;
        this.textBody = textBody;
        this.id = id;
        this.user = user;
        this.like = like;
        this.visit = visit;
        this.postingDate = postingDate;
        this.isLiked = isLiked;
    }
}
