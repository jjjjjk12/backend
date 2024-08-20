package com.ajoudev.backend.repository.post;

import com.ajoudev.backend.dto.post.response.PostPageDTO;
import com.ajoudev.backend.dto.post.response.QPostPageDTO;
import com.ajoudev.backend.entity.member.Member;
import com.ajoudev.backend.entity.member.QMember;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

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
    public Page<PostPageDTO> searchMyPostsPage(Member user, Pageable pageable) {
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
                .from(post)
                .join(post.user, postMember).on(post.user.eq(postMember))
                .where(post.user.eq(user))
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
}
