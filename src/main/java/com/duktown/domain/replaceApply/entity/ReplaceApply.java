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
public class ReplaceApply {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "replace_apply_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "replace_user_id", nullable = false)
    private User replaceUser;

    @Column(nullable = false)
    private LocalDate originDate;

    @Column(nullable = false)
    private String reason;

    @Column(nullable = false)
    private LocalDate replaceDate;

    @Column(nullable = false)
    private Integer state;  // 0 : 대기중, 1 : 승인, 2 : 거절
}
