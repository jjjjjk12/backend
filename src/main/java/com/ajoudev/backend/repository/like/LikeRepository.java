package com.ajoudev.backend.repository.like;

import com.ajoudev.backend.entity.like.LikeIt;
import com.ajoudev.backend.entity.member.Member;
import com.ajoudev.backend.entity.post.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface LikeRepository extends JpaRepository<LikeIt, Long> {

    public Optional<LikeIt> findByUserAndPost(Member user, Post post);
    public boolean existsByUserAndPost(Member user, Post post);

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query(value = "DELETE LikeIt l WHERE l.post = :post")
    public void deleteByPost(Post post);

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query(value = "DELETE LikeIt l WHERE l.user = :user")
    public void deleteByUser(Member user);
}
