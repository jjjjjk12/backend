package com.ajoudev.backend.entity.post;

import com.ajoudev.backend.dto.post.response.PostPageDTO;
import com.ajoudev.backend.dto.post.response.ViewPostDTO;
import com.ajoudev.backend.entity.member.Member;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@Getter
@Entity
@Table(name = "post")
@NoArgsConstructor
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long postNum;
    @Column(nullable = false)
    private String title;
    @Lob
    @Column(columnDefinition = "LONGTEXT", nullable = false)
    private String textBody;
    @Column(nullable = false)
    private String postBoard;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userid")
    private Member user;
    @Column
    private Long likeIt;
    @Column
    private Long visit;
    @Column(nullable = false)
    private LocalDateTime postingDate;
    @Column
    private Long comments;

    public void create(String title, String textBody, String postBoard, Member user) {
        this.title = title;
        this.textBody = textBody;
        this.postBoard = postBoard;
        this.user = user;
        this.likeIt = 0L;
        this.visit = 0L;
        this.comments = 0L;
        this.postingDate = ZonedDateTime.now(ZoneId.of("Asia/Seoul")).toLocalDateTime();
    }

    public void edit(String title, String textBody) {
        this.title = title;
        this.textBody = textBody;
    }

    public Long addVisit() {
        return visit++;
    }
    public Long addComments() {
        return comments++;
    }

    public ViewPostDTO toViewPostDTO() {
        return ViewPostDTO.builder()
                .postNum(postNum)
                .title(title)
                .textBody(textBody)
                .user(user.getNickname())
                .postingDate(postingDate)
                .like(likeIt)
                .visit(visit)
                .build();
    }

    public PostPageDTO toPostPageDTO() {
        return PostPageDTO.builder()
                .postNum(postNum)
                .title(title)
                .user(user.getNickname())
                .postingDate(postingDate)
                .like(likeIt)
                .visit(visit)
                .comment(comments)
                .build();

    }


}
