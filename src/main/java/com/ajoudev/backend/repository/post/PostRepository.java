package com.ajoudev.backend.repository.post;

import com.ajoudev.backend.entity.member.Member;
import com.ajoudev.backend.entity.post.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {
    @Override
    @EntityGraph(attributePaths = {"user"})
    public Page<Post> findAll(Pageable pageable);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query(value = "UPDATE Post p SET p.user = null WHERE p.user = :user ")
    public void updateNullByUser(@Param("user") Member user);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query(value = "UPDATE Post p SET p.comments = p.comments - (SELECT COUNT(c2) FROM  Comment c2 " +
            "WHERE c2.user = :user AND c2.post = p) " +
            "WHERE EXISTS (SELECT 1 FROM Comment c WHERE c.user = :user AND c.post = p)")
    public void updateCommentsByUser(@Param("user") Member user);

}
