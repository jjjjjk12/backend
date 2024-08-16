package com.ajoudev.backend.dto.post.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class PostPageDTO {
    private Long postNum;
    private String title;
    private String user;
    private LocalDateTime postingDate;
    private Long visit;
    private Long like;
    private Long comment;
}
