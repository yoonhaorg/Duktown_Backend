package com.duktown.domain.penaltyPoints;

import com.duktown.domain.BaseTimeEntity;
import com.duktown.domain.user.entity.User;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;

// 벌점
@Entity
public class PenaltyPoints extends BaseTimeEntity {
    @Id @GeneratedValue
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    private String reason;
    private int score;
}
