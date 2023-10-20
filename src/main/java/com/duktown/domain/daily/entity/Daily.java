package com.duktown.domain.daily.entity;

import com.duktown.domain.BaseTimeEntity;
import com.duktown.domain.comment.entity.Comment;
import com.duktown.domain.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import java.util.ArrayList;
import java.util.List;

import static javax.persistence.FetchType.LAZY;
import static lombok.AccessLevel.PRIVATE;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@Builder
@AllArgsConstructor(access = PRIVATE)
@NoArgsConstructor(access = PROTECTED)
public class Daily extends BaseTimeEntity {
    @Id
    @GeneratedValue
    @Column(name = "daily_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(length = 20, nullable = false)
    private String title;

    @Column(nullable = false, columnDefinition = "longtext")
    private String content;

    @OneToMany(mappedBy = "daily")
    private List<Comment> Comments = new ArrayList<>();

    public void update(String title, String content){
        this.title =title;
        this.content =content;
    }
}
