package com.duktown.domain.replaceApply.entity;

import com.duktown.domain.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import java.time.LocalDate;

import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PRIVATE;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@Builder
@AllArgsConstructor(access = PRIVATE)
@NoArgsConstructor(access = PROTECTED)
@Table(name = "replace_apply")
public class ReplaceApply { // 대타구하기
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "replace_apply_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user; //신청인

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "replace_user_id", nullable = false)
    private User replaceUser; // 받는 사람

    @Column(nullable = false)
    private LocalDate originDate; // 신청인 날짜

    @Column(nullable = false)
    private String reason;

    @Column(nullable = false)
    private LocalDate replaceDate; // 교환 날짜

    @Builder.Default
    @Column(nullable = false)
    private int state = 0;   // 0 : 대기중, 1 : 승인, 2 : 거절
}
