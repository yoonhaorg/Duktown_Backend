package com.duktown.domain.unit.entity;

import com.duktown.domain.unitUser.entity.UnitUser;
import com.duktown.domain.user.entity.User;
import com.duktown.global.type.HallName;
import lombok.*;

import javax.persistence.*;

import java.util.ArrayList;
import java.util.List;

import static javax.persistence.EnumType.STRING;
import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Unit { // 기숙사 정보
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "unit_id")
    private Long id;

    @Enumerated(value = STRING)
    @Column(nullable = false)
    private HallName hallName; // 가온1/가온2/국제

    @Column(nullable = false)
    private Integer floorNumber; // 층

    @Column(nullable = false)
    private Integer buildingNumber; // 동

    @Column(nullable = false)
    private Integer roomNumber; // 호

    private Integer occupancy; // 수용 인원

    @OneToMany(mappedBy = "unit", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<User> users = new ArrayList<>(); // 룸메이트

    private Integer unitNumber; // 유닛구분 12명이 1개의 유닛 0~8호 중 0~3호/4~8호

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "unit_user_id", nullable = true)
//    private UnitUser unitUser; // 1개의 Unit 1개의 UnitUser에 속함
}
