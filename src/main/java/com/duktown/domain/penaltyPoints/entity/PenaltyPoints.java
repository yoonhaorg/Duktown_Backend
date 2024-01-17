package com.duktown.domain.penaltyPoints.entity;

import com.duktown.domain.BaseTimeEntity;
import com.duktown.domain.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import java.time.LocalDate;

import static javax.persistence.FetchType.LAZY;

// 벌점
@Getter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PenaltyPoints extends BaseTimeEntity {
    @Id @GeneratedValue
    private Long id;
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    private String reason;
    private int score;
    private LocalDate date;

}
