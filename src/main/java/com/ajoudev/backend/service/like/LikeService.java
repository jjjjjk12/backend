package com.ajoudev.backend.service.like;

import com.ajoudev.backend.dto.post.response.ViewAnswerDTO;
import com.ajoudev.backend.dto.post.response.ViewPostDTO;
import com.ajoudev.backend.entity.like.Dislike;
import com.ajoudev.backend.entity.like.LikeIt;
import com.ajoudev.backend.entity.member.Member;
import com.ajoudev.backend.entity.post.AnswerPost;
import com.ajoudev.backend.entity.post.Post;
import com.ajoudev.backend.exception.like.LikeException;
import com.ajoudev.backend.exception.member.NotFoundUserException;
import com.ajoudev.backend.repository.like.DislikeRepository;
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
    private final DislikeRepository dislikeRepository;

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

    public ViewAnswerDTO addLikeToAnswer(Long postNum) throws RuntimeException{

        String id = SecurityContextHolder.getContext().getAuthentication().getName();
        Member member = memberRepository.findByUserid(id).orElse(null);
        AnswerPost post = (AnswerPost) postRepository.findById(postNum).orElse(null);

        if (member == null) throw new NotFoundUserException("ERR_USER_NOT_FOUND");
        if (post == null) throw new LikeException();

        LikeIt like = likeRepository.findByUserAndPost(member, post).orElse(null);
        if (like != null){
            likeRepository.delete(like);
            post.cancelLike();
            return post.toViewAnswerDTO(false, dislikeRepository.existsByUserAndPost(member, post));
        }

        LikeIt newLike = new LikeIt();
        newLike.create(member, post);
        post.addLikeIt();

        likeRepository.save(newLike);
        return post.toViewAnswerDTO(true, dislikeRepository.existsByUserAndPost(member, post));
    }

    public ViewAnswerDTO addDislikeToAnswer(Long postNum) throws RuntimeException{


        String id = SecurityContextHolder.getContext().getAuthentication().getName();
        Member member = memberRepository.findByUserid(id).orElse(null);
        AnswerPost post = (AnswerPost) postRepository.findById(postNum).orElse(null);

        if (member == null) throw new NotFoundUserException("ERR_USER_NOT_FOUND");
        if (post == null) throw new LikeException();

        Dislike dislike = dislikeRepository.findByUserAndPost(member, post).orElse(null);
        if (dislike != null){
            dislikeRepository.delete(dislike);
            post.cancelDislike();
            return post.toViewAnswerDTO(likeRepository.existsByUserAndPost(member, post), false);
        }

        Dislike newDislike = new Dislike();
        newDislike.create(member, post);
        post.addDislike();

        dislikeRepository.save(newDislike);
        return post.toViewAnswerDTO(likeRepository.existsByUserAndPost(member, post), true);
    }

}
