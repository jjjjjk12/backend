package com.ajoudev.backend.dto.post.response;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Builder
public class PostPageDTO {
    private Long postNum;
    private String title;
    private String user;
    private String id;
    private LocalDateTime postingDate;
    private Long visit;
    private Long like;
    private Long comment;
    private String board;

    @QueryProjection
    public PostPageDTO(Long postNum, String title, String user, String id, LocalDateTime postingDate, Long visit, Long like, Long comment, String board) {
        this.postNum = postNum;
        this.title = title;
        this.user = user;
        this.id = id;
        this.postingDate = postingDate;
        this.visit = visit;
        this.like = like;
        this.comment = comment;
        this.board = board;
    }
}
