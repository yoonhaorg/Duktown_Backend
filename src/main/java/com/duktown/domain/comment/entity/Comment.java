package com.duktown.domain.comment.entity;

import com.duktown.domain.BaseTimeEntity;
import com.duktown.domain.post.entity.Post;
import com.duktown.domain.delivery.entity.Delivery;
import com.duktown.domain.like.entity.Like;
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
public class Comment extends BaseTimeEntity {
    @Id
    @GeneratedValue
    @Column(name = "comment_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "delivery_id")
    private Delivery delivery;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "parent_comment_id")
    private Comment parentComment;  // 대댓글인 경우

    @OneToMany(mappedBy = "parentComment")
    private List<Comment> childComments = new ArrayList<>();

    @Column(nullable = false)
    private String content; // 255자 최대

    @OneToMany(mappedBy = "comment")
    private List<Like> likes = new ArrayList<>();

    @Column(nullable = false)
    private Boolean deleted;

    public void update(String content) {
        this.content = content;
    }

    public void deleteWithChildComment () {
        this.content = "[사용자에 의해 삭제된 댓글입니다.]";
        this.deleted = true;
    }
}
