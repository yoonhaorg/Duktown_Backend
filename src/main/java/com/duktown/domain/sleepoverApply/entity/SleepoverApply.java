package com.duktown.domain.sleepoverApply.entity;

import com.duktown.domain.user.entity.User;
import lombok.*;

import javax.persistence.*;

import java.time.LocalDate;

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
public class SleepoverApply {
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

    @Column(nullable = false)
    private String reason; //사유

    private Boolean approved; //승인 여부

    public void approve(boolean approved){
        this.approved = approved;
    }
}
