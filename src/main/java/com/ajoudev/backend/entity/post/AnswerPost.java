package com.ajoudev.backend.entity.post;

import com.ajoudev.backend.dto.post.response.ViewAnswerDTO;
import com.ajoudev.backend.entity.member.Member;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;

import java.time.ZoneId;
import java.time.ZonedDateTime;

@Entity
@DiscriminatorValue("ANSWER")
@Getter
public class AnswerPost extends Post {
    @ManyToOne
    @JoinColumn(name = "parent_num", referencedColumnName = "postNum")
    QuestionPost parent;

    boolean isAdopted;

    Long dislikes;

    public void create(String title, String textBody, String postBoard, Member user, QuestionPost parent) {
        super.create(title, textBody, postBoard, user);
        this.isAdopted = false;
        this.dislikes = 0L;
        this.parent = parent;
    }

    public boolean adopted() { return isAdopted = true; }

    public Long cancelDislike(){ return dislikes--; }
    public Long addDislike(){ return dislikes++; }

    public ViewAnswerDTO toViewAnswerDTO(boolean isLiked, boolean isDisliked) {
        return ViewAnswerDTO.builder()
                .postNum(postNum)
                .title(title)
                .textBody(textBody)
                .user(user.getNickname())
                .postingDate(postingDate)
                .like(likeIt)
                .dislike(dislikes)
                .id(user.getUserid())
                .isLiked(isLiked)
                .isDisliked(isDisliked)
                .isAdopted(isAdopted)
                .build();

    }

}
