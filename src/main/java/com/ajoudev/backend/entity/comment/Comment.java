package com.ajoudev.backend.entity.comment;

import com.ajoudev.backend.dto.comment.response.CommentPageDTO;
import com.ajoudev.backend.entity.member.Member;
import com.ajoudev.backend.entity.post.Post;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@Getter
@Entity
@Table(name = "comment")
@NoArgsConstructor
public class Comment {

    @Lob
    @Column(columnDefinition = "LONGTEXT", nullable = false)
    private String commentBody;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "postNum", nullable = false)
    private Post post;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "commentNum", nullable = false)
    private Long commentNum;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userid")
    private Member user;

    @Column(nullable = false)
    private LocalDateTime commentingDate;

    public void create(String commentBody, Post post, Member user) {
        this.commentBody = commentBody;
        this.post = post;
        this.user = user;
        this.commentingDate = ZonedDateTime.now(ZoneId.of("Asia/Seoul")).toLocalDateTime();

    }

    public CommentPageDTO toPageCommentDTO() {
        return CommentPageDTO.builder()
                .commentNum(commentNum)
                .commentBody(commentBody)
                .commentingDate(commentingDate)
                .user(user.getNickname())
                .build();
    }

    public void edit(String commentBody) {
        this.commentBody = commentBody;
    }
}
