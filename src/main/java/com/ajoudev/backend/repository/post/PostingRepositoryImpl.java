package com.ajoudev.backend.repository.post;

import com.ajoudev.backend.dto.post.response.*;
import com.ajoudev.backend.entity.like.QDislike;
import com.ajoudev.backend.entity.member.Member;
import com.ajoudev.backend.entity.member.QMember;
import com.ajoudev.backend.entity.post.Post;
import com.ajoudev.backend.entity.post.QAnswerPost;
import com.ajoudev.backend.entity.post.QQuestionPost;
import com.ajoudev.backend.entity.post.QuestionPost;
import com.querydsl.core.types.ConstantImpl;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.data.support.PageableExecutionUtils;

import java.time.LocalDateTime;
import java.util.List;

import static com.ajoudev.backend.entity.like.QLikeIt.*;
import static com.ajoudev.backend.entity.post.QPost.post;

@RequiredArgsConstructor
public class PostingRepositoryImpl implements PostingRepository{
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Page<PostPageDTO> searchLikedPostsPage(Member user, Pageable pageable) {
        QMember postMember = new QMember("postMember");
        List<PostPageDTO> content = jpaQueryFactory
                .select(new QPostPageDTO(
                        post.postNum,
                        post.title,
                        post.user.nickname,
                        post.user.userid,
                        post.postingDate,
                        post.visit,
                        post.likeIt,
                        post.comments
                ))
                .from(likeIt)
                .join(likeIt.post, post).on(likeIt.post.eq(post))
                .join(likeIt.post.user, postMember).on(likeIt.post.user.eq(postMember))
                .where(likeIt.user.eq(user))
                .orderBy(likeIt.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> cnt = jpaQueryFactory
                .select(likeIt.count())
                .from(likeIt)
                .where(likeIt.user.eq(user));

        return PageableExecutionUtils.getPage(content, pageable, cnt::fetchOne);
    }

    @Override
    public Page<PostPageDTO> searchMyPostsPage(Member user, Pageable pageable, String board) {
        QMember postMember = new QMember("postMember");
        BooleanExpression boardEQ = board != null ? post.postBoard.eq(board) : null;
        List<PostPageDTO> content = jpaQueryFactory
                .select(new QPostPageDTO(
                        post.postNum,
                        post.title,
                        post.user.nickname,
                        post.user.userid,
                        post.postingDate,
                        post.visit,
                        post.likeIt,
                        post.comments
                ))
                .from(post)
                .join(post.user, postMember).on(post.user.eq(postMember))
                .where(post.user.eq(user), boardEQ)
                .orderBy(post.postNum.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> cnt = jpaQueryFactory
                .select(post.count())
                .from(post)
                .where(post.user.eq(user));

        return PageableExecutionUtils.getPage(content, pageable, cnt::fetchOne);
    }

    @Override
    public Page<QuestionPageDTO> searchQuestions(Pageable pageable) {
        QMember postMember = new QMember("postMember");
        QQuestionPost quest = new QQuestionPost("question");
        List<QuestionPageDTO> content = jpaQueryFactory
                .select(new QQuestionPageDTO(
                        quest.postNum,
                        quest.title,
                        quest.user.nickname,
                        quest.user.userid,
                        quest.postingDate,
                        quest.visit,
                        quest.likeIt,
                        quest.answers,
                        quest.comments
                ))
                .from(quest)
                .join(quest.user, postMember).on(quest.user.eq(postMember))
                .orderBy(quest.postNum.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> cnt = jpaQueryFactory
                .select(quest.count())
                .from(quest);

        return PageableExecutionUtils.getPage(content, pageable, cnt::fetchOne);
    }

    @Override
    public Slice<AnswerPageDTO> searchAnswers(Pageable pageable, QuestionPost parent, Member user) {
        QMember postMember = new QMember("postMember");
        QAnswerPost answer = new QAnswerPost("answer");
        BooleanExpression isLiked = user != null ? JPAExpressions
                .selectOne()
                .from(likeIt)
                .join(likeIt.post, post)
                .join(likeIt.user, QMember.member)
                .where(likeIt.post.postNum.eq(answer.postNum), likeIt.user.eq(user))
                .exists() : Expressions.FALSE;
        BooleanExpression isDisliked = user != null ? JPAExpressions
                .selectOne()
                .from(QDislike.dislike)
                .join(QDislike.dislike.post, post)
                .join(QDislike.dislike.user, QMember.member)
                .where(QDislike.dislike.post.postNum.eq(answer.postNum), QDislike.dislike.user.eq(user))
                .exists() : Expressions.FALSE;

        List<AnswerPageDTO> content = jpaQueryFactory
                .select(new QAnswerPageDTO(
                        answer.postNum,
                        answer.title,
                        answer.textBody,
                        answer.user.nickname,
                        answer.user.userid,
                        answer.postingDate,
                        answer.likeIt,
                        answer.dislikes,
                        answer.comments,
                        isLiked,
                        isDisliked,
                        answer.isAdopted
                ))
                .from(answer)
                .join(answer.user, postMember).on(answer.user.eq(postMember))
                .where(answer.parent.eq(parent))
                .orderBy(answer.isAdopted.desc(), answer.likeIt.desc(), answer.dislikes.asc() ,answer.postNum.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize() + 1)
                .fetch();

        boolean hasNext = content.size() > pageable.getPageSize();

        if (hasNext) {
            content.removeLast();
        }

        return new SliceImpl<>(content, pageable, hasNext);
    }

    @Override
    public ViewAnswerDTO findAnswerByUserAndPostNum(Member user, Long postNum) {
        QAnswerPost answer = new QAnswerPost("answer");
        QMember postMember = new QMember("postMember");
        BooleanExpression isLiked = user != null ? JPAExpressions
                .selectOne()
                .from(likeIt)
                .join(likeIt.post, post)
                .join(likeIt.user, QMember.member)
                .where(likeIt.post.postNum.eq(postNum), likeIt.user.eq(user))
                .exists() : Expressions.FALSE;
        BooleanExpression isDisliked = user != null ? JPAExpressions
                .selectOne()
                .from(QDislike.dislike)
                .join(QDislike.dislike.post, post)
                .join(QDislike.dislike.user, QMember.member)
                .where(QDislike.dislike.post.postNum.eq(postNum), QDislike.dislike.user.eq(user))
                .exists() : Expressions.FALSE;

        return  jpaQueryFactory
                .select(new QViewAnswerDTO(
                        answer.postNum,
                        answer.title,
                        answer.textBody,
                        answer.user.nickname,
                        answer.user.userid,
                        answer.postingDate,
                        answer.likeIt,
                        answer.dislikes,
                        answer.comments,
                        isLiked,
                        isDisliked,
                        answer.isAdopted
                ))
                .from(answer)
                .join(answer.user, postMember)
                .where(answer.user.eq(postMember), answer.postNum.eq(postNum))
                .fetchOne();
    }
}
