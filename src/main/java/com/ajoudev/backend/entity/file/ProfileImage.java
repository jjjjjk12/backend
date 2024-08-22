package com.ajoudev.backend.entity.file;

import com.ajoudev.backend.entity.member.Member;
import com.ajoudev.backend.entity.post.Post;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Entity
@Table
@NoArgsConstructor
public class ProfileImage {

    @Column(nullable = false)
    private String filename;

    @Column(nullable = false)
    private String filename_original;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long imageNum;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userid")
    private Member user;

    @Column(nullable = false)
    private String file_path;

    @Column(nullable = false)
    private String mime_type;

    public void create(String filename, String filename_original, String file_path, String mime_type, Member user) {
        this.filename = filename;
        this.filename_original = filename_original;
        this.file_path = file_path;
        this.mime_type = mime_type;
        this.user = user;


    }

}
