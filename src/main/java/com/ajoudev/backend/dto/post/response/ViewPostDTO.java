package com.ajoudev.backend.dto.post.response;

import com.ajoudev.backend.entity.member.Member;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class ViewPostDTO {
    private Long postNum;
    private String title;
    private String textBody;
    private String id;
    private String user;
    private Long like;
    private Long visit;
    private LocalDateTime postingDate;
}
