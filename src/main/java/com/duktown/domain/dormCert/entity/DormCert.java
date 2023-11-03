package com.duktown.domain.dormCert.entity;

import com.duktown.domain.user.entity.User;
import com.duktown.global.type.CertRequestType;
import com.duktown.global.type.HallName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import static javax.persistence.EnumType.*;
import static javax.persistence.FetchType.*;
import static javax.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PRIVATE;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@Builder
@AllArgsConstructor(access = PRIVATE)
@NoArgsConstructor(access = PROTECTED)
@Table(name = "dorm_cert")
public class DormCert {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "dorm_cert_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // 인증 종류
    @Enumerated(STRING)
    @Column(nullable = false)
    private CertRequestType certRequestType;

    // 이미지
    @Column(nullable = false)
    private String imgUrl;

    // 학번
    @Column(nullable = false)
    private String studentId;

    // 기숙사 정보
    @Enumerated(value = STRING)
    @Column(nullable = false)
    private HallName hallName;

    @Column(nullable = false)
    private Integer floorNumber;

    @Column(nullable = false)
    private Integer buildingNumber;

    @Column(nullable = false)
    private Integer roomNumber;

    // 인증 여부
    private Boolean certified;

    public void update(Boolean certified){
        this.certified = certified;
    }
}
