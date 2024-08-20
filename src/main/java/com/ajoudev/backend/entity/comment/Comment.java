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

    @Column
    private Long parentComment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userid")
    private Member user;

    @Column(nullable = false)
    private LocalDateTime commentingDate;

    public void createComment(String commentBody, Post post, Member user) {
        this.commentBody = commentBody;
        this.parentComment = null;
        this.post = post;
        this.user = user;
        this.commentingDate = ZonedDateTime.now(ZoneId.of("Asia/Seoul")).toLocalDateTime();

    }

    public void editComment(String commentBody) {
        this.commentBody = commentBody;
    }

    public void createReply(String commentBody, Post post, Member user, Long parentComment) {
        this.commentBody = commentBody;
        this.parentComment = parentComment;
        this.post = post;
        this.user = user;
        this.commentingDate = ZonedDateTime.now(ZoneId.of("Asia/Seoul")).toLocalDateTime();
    }

    @PostPersist
    public void prePersist() {
        if (this.parentComment == null) {
            this.parentComment = this.commentNum;
        }
    }

}
