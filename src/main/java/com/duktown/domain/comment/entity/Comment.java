package com.duktown.domain.comment.entity;

import com.duktown.domain.daily.entity.Daily;
import com.duktown.domain.delivery.entity.Delivery;
import com.duktown.domain.market.entity.Market;
import com.duktown.domain.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;
import static lombok.AccessLevel.PRIVATE;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@Builder
@AllArgsConstructor(access = PRIVATE)
@NoArgsConstructor(access = PROTECTED)
public class Comment {
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
    @JoinColumn(name = "daily_id")
    private Daily daily;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "market_id")
    private Market market;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "parent_comment_id")
    private Comment parentComment;  // 대댓글인 경우

    @Column(nullable = false)
    private String content; // 255자 최대

    @Column(nullable = false)
    private Boolean deleted;
}
