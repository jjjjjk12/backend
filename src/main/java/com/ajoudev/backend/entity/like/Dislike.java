package com.ajoudev.backend.entity.like;

import com.ajoudev.backend.entity.member.Member;
import com.ajoudev.backend.entity.post.Post;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table
@NoArgsConstructor
public class Dislike {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userid", nullable = false)
    private Member user;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "postNum", nullable = false)
    private Post post;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, name = "dislikeNum")
    private Long id;

    public void create(Member user, Post post) {
        this.user = user;
        this.post = post;
    }
}
