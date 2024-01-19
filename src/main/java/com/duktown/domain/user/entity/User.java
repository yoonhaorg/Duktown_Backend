package com.duktown.domain.user.entity;

import com.duktown.domain.BaseTimeEntity;
import com.duktown.domain.unit.entity.Unit;
import com.duktown.global.exception.CustomErrorType;
import com.duktown.global.exception.CustomException;
import com.duktown.global.type.RoleType;
import com.duktown.global.type.UnitUserType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import static javax.persistence.EnumType.*;
import static javax.persistence.GenerationType.*;
import static lombok.AccessLevel.*;

@Entity
@Getter
@Builder
@AllArgsConstructor(access = PRIVATE)
@NoArgsConstructor(access = PROTECTED)
@Table(name = "users")
public class User extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String loginId;

    @Column(nullable = false)
    private String password;

    @Builder.Default
    @Enumerated(value = STRING)
    @Column(nullable = false)
    private RoleType roleType = RoleType.DORM_STUDENT;

    private String refreshToken;

    //TODO: 외박 일 수 관련해서 질문하기
    // 남은 외박 일 수
    private Integer availablePeriod;

    public void updateRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public void deleteRefreshToken() {
        this.refreshToken = null;
    }

    public void updatePassword(String encodedPwd) {
        this.password = encodedPwd;
    }

    @PrePersist
    private void baseAvailablePeriod(){
        // 외박 가능 일수 초기값 부여
        availablePeriod = 16;
    }

    public void downAvailablePeriod(Integer period){
        // 외박 가능 일수 다운
            this.availablePeriod -= period;
    }



}
