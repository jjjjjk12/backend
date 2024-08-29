package com.ajoudev.backend.entity.post;

import com.ajoudev.backend.entity.member.Member;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;

@Entity
@DiscriminatorValue("QUESTION")
@Getter
public class QuestionPost extends Post {
    private Long answers;
    private boolean hasAdopted;


    @Override
    public void create(String title, String textBody, String postBoard, Member user) {
        super.create(title, textBody, postBoard, user);
        this.answers = 0L;
        hasAdopted = false;
    }

    public Long addQuestion() {return answers++;}
    public Long deleteQuestion() {return answers++;}
    public boolean doAdopt() {return hasAdopted = true;}
}
