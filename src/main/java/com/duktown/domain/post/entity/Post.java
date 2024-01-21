package com.duktown.domain.post.entity;

import com.duktown.domain.BaseTimeEntity;
import com.duktown.domain.comment.entity.Comment;
import com.duktown.domain.like.entity.Like;
import com.duktown.domain.user.entity.User;
import com.duktown.global.type.Category;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Where;

import javax.persistence.*;

import java.util.ArrayList;
import java.util.List;

import static javax.persistence.EnumType.STRING;
import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PRIVATE;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@Builder
@AllArgsConstructor(access = PRIVATE)
@NoArgsConstructor(access = PROTECTED)
public class Post extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "post_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated(STRING)
    @Column(nullable = false)
    private Category category;

    @Column(length = 20, nullable = false)
    private String title;

    @Column(nullable = false, columnDefinition = "longtext")
    private String content;

    @Builder.Default
    @OneToMany(fetch = LAZY, mappedBy = "post")
    private List<Comment> comments = new ArrayList<>();

//
//    @OneToMany(fetch = LAZY, mappedBy = "post")
//    private List<Like> likes
    @Builder.Default
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Like> likes = new ArrayList<>();;


    public void update(String title, String content){
        this.title =title;
        this.content =content;
    }
}
