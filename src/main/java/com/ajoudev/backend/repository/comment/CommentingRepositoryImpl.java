package com.ajoudev.backend.repository.comment;

import com.ajoudev.backend.dto.comment.response.CommentPageDTO;
import com.ajoudev.backend.dto.comment.response.MyCommentPageDTO;
import com.ajoudev.backend.dto.comment.response.QCommentPageDTO;
import com.ajoudev.backend.dto.comment.response.QMyCommentPageDTO;
import com.ajoudev.backend.entity.member.Member;
import com.ajoudev.backend.entity.member.QMember;
import com.ajoudev.backend.entity.post.Post;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import java.util.List;

import static com.ajoudev.backend.entity.comment.QComment.*;
import static com.ajoudev.backend.entity.post.QPost.*;

@RequiredArgsConstructor
public class CommentingRepositoryImpl implements CommentingRepository {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Page<CommentPageDTO> searchPage(Post post, Pageable pageable) {
        QMember commentMember = new QMember("commentMember");
        List<CommentPageDTO> content = jpaQueryFactory
                .select(new QCommentPageDTO(
                        comment.commentBody,
                        comment.commentNum,
                        comment.parentComment,
                        comment.user.nickname,
                        comment.user.userid,
                        comment.commentingDate
                ))
                .from(comment)
                .leftJoin(comment.user, commentMember)
                .where(comment.post.eq(post))
                .orderBy(comment.parentComment.asc(), comment.commentNum.asc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> cnt = jpaQueryFactory
                .select(comment.count())
                .from(comment)
                .where(comment.post.eq(post));

        return PageableExecutionUtils.getPage(content, pageable, cnt::fetchOne);


    }

    @Override
    public Page<MyCommentPageDTO> searchMyCommentPage(Member user, Pageable pageable) {
        QMember commentMember = new QMember("commentMember");
        List<MyCommentPageDTO> content = jpaQueryFactory
                .select(new QMyCommentPageDTO(
                        comment.commentBody,
                        comment.commentNum,
                        comment.user.nickname,
                        comment.user.userid,
                        comment.commentingDate,
                        post.title,
                        post.postNum,
                        post.postBoard
                ))
                .from(comment)
                .join(comment.user, commentMember).on(comment.user.eq(commentMember))
                .join(comment.post, post).on(comment.post.eq(post))
                .where(comment.user.eq(user))
                .orderBy(comment.commentNum.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> cnt = jpaQueryFactory
                .select(comment.count())
                .from(comment)
                .where(comment.user.eq(user));

        return PageableExecutionUtils.getPage(content, pageable, cnt::fetchOne);
    }
}
