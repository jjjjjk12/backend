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

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query(value = "Update Comment c SET c.user = null, c.commentBody = '삭제된 댓글입니다' WHERE c.commentNum = :id")
    public void updateNull(@Param("id") Long commentNum);

    @EntityGraph(attributePaths = {"post", "user"})
    public Optional<Comment> findByCommentNum(Long commentNum);

    public Long countByParentComment(Long ParentComment);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query(value = "DELETE Comment c WHERE c.post = :post")
    public void deleteByPost(Post post);
}
