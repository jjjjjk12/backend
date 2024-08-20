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

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query(value = "UPDATE comment c JOIN (SELECT parent_comment, COUNT(parent_comment) " +
            "AS cnt FROM comment GROUP BY parent_comment) child ON c.comment_num = child.parent_comment " +
            "SET c.userid = null, c.comment_body = '삭제된 댓글입니다' WHERE child.cnt > 1 AND c.userid = :user", nativeQuery = true)
    public void updateNullByUser(@Param("user") String user);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query(value = "DELETE Comment c " +
            "WHERE c.user = :user")
    public void deleteByUser(@Param("user") Member user);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query(value = "Update Comment c SET c.user = null, c.commentBody = '삭제된 댓글입니다' WHERE c.commentNum = :id")
    public void updateNull(@Param("id") Long commentNum);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query(value = "DELETE c FROM comment AS c JOIN (SELECT parent_comment, COUNT(parent_comment) " +
            "AS cnt FROM comment GROUP BY parent_comment) child ON c.comment_num = child.parent_comment " +
            "WHERE child.cnt <= 1 AND c.userid IS NULL", nativeQuery = true)
    public void deleteAllNulls();

    @EntityGraph(attributePaths = {"post", "user"})
    public Optional<Comment> findByCommentNum(Long commentNum);

    public Long countByParentComment(Long ParentComment);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query(value = "DELETE Comment c WHERE c.post = :post")
    public void deleteByPost(Post post);

}
