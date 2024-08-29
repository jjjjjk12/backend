package com.ajoudev.backend.entity.post;

import com.ajoudev.backend.dto.post.response.PostPageDTO;
import com.ajoudev.backend.dto.post.response.ViewPostDTO;
import com.ajoudev.backend.entity.member.Member;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@Getter
@Entity
@Table(name = "post")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn
@NoArgsConstructor
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    protected Long postNum;
    @Column(nullable = false)
    protected String title;
    @Lob
    @Column(columnDefinition = "LONGTEXT", nullable = false)
    protected String textBody;
    @Column(nullable = false)
    protected String postBoard;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userid")
    protected Member user;
    @Column
    protected Long likeIt;
    @Column
    protected Long visit;
    @Column(nullable = false)
    protected LocalDateTime postingDate;
    @Column
    protected Long comments;

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
    public Long addLikeIt() {
        return likeIt++;
    }

    public Long cancelLike(){ return likeIt--; }

    public Long deleteComments(){ return  comments--; }

    public ViewPostDTO toViewPostDTO(boolean isLiked) {
        return ViewPostDTO.builder()
                .postNum(postNum)
                .title(title)
                .textBody(textBody)
                .user(user.getNickname())
                .postingDate(postingDate)
                .like(likeIt)
                .visit(visit)
                .id(user.getUserid())
                .isLiked(isLiked)
                .build();
    }

    public PostPageDTO toPostPageDTO() {
        return PostPageDTO.builder()
                .postNum(postNum)
                .title(title)
                .user(user == null ? null : user.getNickname())
                .postingDate(postingDate)
                .like(likeIt)
                .visit(visit)
                .comment(comments)
                .id(user == null ? null : user.getUserid())
                .build();

    }

}
