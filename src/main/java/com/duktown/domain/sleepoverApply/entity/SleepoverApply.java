package com.duktown.domain.sleepoverApply.entity;

import com.duktown.domain.BaseTimeEntity;
import com.duktown.domain.user.entity.User;
import com.duktown.global.type.ApprovalType;
import lombok.*;

import javax.persistence.*;

import java.time.LocalDate;

import static javax.persistence.EnumType.STRING;
import static javax.persistence.FetchType.*;
import static javax.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PRIVATE;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@Builder
@AllArgsConstructor(access = PRIVATE)
@NoArgsConstructor(access = PROTECTED)
@Table(name = "sleepover_apply")
public class SleepoverApply extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "sleepover_apply_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private LocalDate startDate; //외박 시작 날짜

    @Column(nullable = false)
    private LocalDate endDate; //돌아오는 날짜

    @Column(nullable = false)
    private Integer period; //외박 일 수

    @Column(nullable = false)
    private String address; // 머무르는 주소

    // 남은 외박 일 수
    private Integer totalPeriod;

    @Column(nullable = false)
    private String reason; //사유

    @Enumerated(STRING)
    private ApprovalType approved; //승인 여부

    public void approve(ApprovalType approved){
        this.approved = approved;
    }

    @PrePersist
    private void baseTotoalPeriod(){
        // 외박 가능 일수 초기값 부여
        totalPeriod = 21;
    };


}
