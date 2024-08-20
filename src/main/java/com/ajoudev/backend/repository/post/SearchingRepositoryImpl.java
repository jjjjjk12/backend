package com.ajoudev.backend.repository.post;

import com.ajoudev.backend.dto.post.response.PostPageDTO;
import com.ajoudev.backend.dto.post.response.QPostPageDTO;
import com.ajoudev.backend.dto.search.request.SearchPostDTO;
import com.ajoudev.backend.entity.member.QMember;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import java.util.List;

import static com.ajoudev.backend.entity.post.QPost.post;

@RequiredArgsConstructor
public class SearchingRepositoryImpl implements SearchingRepository {
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Page<PostPageDTO> searchByRequest(SearchPostDTO filterDTO, Pageable pageable) {
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
                .join(post.user, QMember.member).on(post.user.eq(QMember.member))
                .where(getFilter(filterDTO))
                .orderBy(post.postNum.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> cnt = jpaQueryFactory
                .select(post.count())
                .from(post)
                .where(getFilter(filterDTO));

        return PageableExecutionUtils.getPage(content, pageable, cnt::fetchOne);
    }

    private BooleanBuilder getFilter(SearchPostDTO dto) {
        BooleanBuilder builder = new BooleanBuilder();

        if (dto.getBoard() != null) {
            builder.and(post.postBoard.eq(dto.getBoard()));
        }

        if (dto.getTitle() != null || dto.getText() != null || dto.getUser() != null) {
            BooleanBuilder sub = new BooleanBuilder();
            if (dto.getUser() != null) {
                builder.or(post.user.nickname.contains(dto.getKeyword()));
            }
            if (dto.getText() != null) {
                for (String str : dto.getParse()) {
                    builder.or(post.textBody.contains(str));
                }
            }
            if (dto.getTitle() != null) {
                for (String str : dto.getParse()) {
                    builder.or(post.title.contains(str));
                }
            }
            builder.and(sub);
        }


        return builder;
    }
}
