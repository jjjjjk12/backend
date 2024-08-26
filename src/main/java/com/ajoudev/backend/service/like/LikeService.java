package com.ajoudev.backend.service.like;

import com.ajoudev.backend.dto.post.response.ViewPostDTO;
import com.ajoudev.backend.entity.like.LikeIt;
import com.ajoudev.backend.entity.member.Member;
import com.ajoudev.backend.entity.post.Post;
import com.ajoudev.backend.exception.like.LikeException;
import com.ajoudev.backend.exception.member.NotFoundUserException;
import com.ajoudev.backend.repository.like.LikeRepository;
import com.ajoudev.backend.repository.member.MemberRepository;
import com.ajoudev.backend.repository.post.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class LikeService {

    private final LikeRepository likeRepository;
    private final MemberRepository memberRepository;
    private final PostRepository postRepository;

    public ViewPostDTO addLike(Long postNum) throws RuntimeException{

        String id = SecurityContextHolder.getContext().getAuthentication().getName();
        Member member = memberRepository.findByUserid(id).orElse(null);
        Post post = postRepository.findById(postNum).orElse(null);

        if (member == null) throw new NotFoundUserException("ERR_USER_NOT_FOUND");
        if (post == null) throw new LikeException();

        LikeIt like = likeRepository.findByUserAndPost(member, post).orElse(null);
        if (like != null){
            likeRepository.delete(like);
            post.cancelLike();
            return post.toViewPostDTO(false);
        }

        LikeIt newLike = new LikeIt();
        newLike.create(member, post);
        post.addLikeIt();

        likeRepository.save(newLike);
        return post.toViewPostDTO(true);

    }

}
