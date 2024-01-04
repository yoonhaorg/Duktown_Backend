package com.duktown.domain.penaltyPoints;

import com.duktown.domain.BaseTimeEntity;
import com.duktown.domain.user.entity.User;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class penaltyPoints extends BaseTimeEntity {
    @Id @GeneratedValue
    private Long id;

    private User user;
    private String reason;
    private int score;
}
// 벌저