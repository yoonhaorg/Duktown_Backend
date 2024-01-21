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
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;

import java.time.LocalDateTime;

import static javax.persistence.EnumType.*;
import static javax.persistence.GenerationType.*;
import static lombok.AccessLevel.*;

@Entity
@Getter
@Builder
@AllArgsConstructor(access = PRIVATE)
@NoArgsConstructor(access = PROTECTED)
@Table(name = "users")
@SQLDelete(sql = "UPDATE users SET deleted = true, deleted_at = CURRENT_TIMESTAMP, " +
        "name = '(탈퇴한 회원)', email = null, login_id = null, " +
        "password = null, refresh_token = null, role_type = null, available_period = null " +
        "WHERE user_id = ?")
public class User extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "user_id")
    private Long id;

    private String name;

    private String email;

    private String loginId;

    private String password;

    @Builder.Default
    @Enumerated(value = STRING)
    private RoleType roleType = RoleType.DORM_STUDENT;

    private String refreshToken;

    //TODO: 외박 일 수 관련해서 질문하기
    // 남은 외박 일 수
    @Builder.Default
    private Integer availablePeriod = 16;

    @Builder.Default
    @Column(nullable = false)
    private boolean deleted = false;

    private LocalDateTime deletedAt;

    public void updateRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public void deleteRefreshToken() {
        this.refreshToken = null;
    }

    public void updatePassword(String encodedPwd) {
        this.password = encodedPwd;
    }

    public void downAvailablePeriod(Integer period){
        // 외박 가능 일수 다운
        this.availablePeriod -= period;
    }
}
