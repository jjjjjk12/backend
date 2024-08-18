package com.ajoudev.backend.repository.comment;

import com.ajoudev.backend.entity.comment.Comment;
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

public interface CommentRepository extends JpaRepository<Comment, Long>, CommentingRepository {
    @EntityGraph(attributePaths = {"user"})
    public Page<Comment> findPageByPost(Post post, Pageable pageable);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query(value = "UPDATE Comment c SET c.user = null WHERE c.user = :user ")
    public void updateNullByUser(@Param("user") Member user);

    @EntityGraph(attributePaths = {"post", "user"})
    public Optional<Comment> findByCommentNum(Long commentNum);
}
