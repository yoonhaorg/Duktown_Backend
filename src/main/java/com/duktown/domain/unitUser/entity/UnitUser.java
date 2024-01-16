package com.duktown.domain.unitUser.entity;

import com.duktown.domain.BaseTimeEntity;
import com.duktown.domain.unit.entity.Unit;
import com.duktown.domain.user.entity.User;
import com.duktown.global.type.UnitUserType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import static javax.persistence.EnumType.STRING;
import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PRIVATE;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@Builder
@AllArgsConstructor(access = PRIVATE)
@NoArgsConstructor(access = PROTECTED)
@Table(name = "unit_users")
public class UnitUser extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "unit_user_id")
    private Long id;

//    @ManyToOne(fetch = LAZY)
//    @JoinColumn(name = "user_id", nullable = false)
//    private User user;
//
//    @ManyToOne(fetch = LAZY)
//    @JoinColumn(name = "unit_id", nullable = false)
//    private Unit unit;// 배정 정보
//
    @Enumerated(STRING)
    @Column(nullable = false)
    private UnitUserType unitUserType;
}
