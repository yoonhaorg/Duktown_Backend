package com.duktown.domain.unit.entity;

import com.duktown.domain.unitUser.entity.UnitUser;
import com.duktown.domain.user.entity.User;
import com.duktown.global.type.HallName;
import lombok.*;

import javax.persistence.*;

import java.util.ArrayList;
import java.util.List;

import static javax.persistence.EnumType.STRING;
import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Unit {
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
    private Integer buildingNumber; // 동 1=A, 2=B

    @Column(nullable = false)
    private Integer roomNumber; // 호

    @Builder.Default
    private int occupancy = 4; // 수용 인원

    private int currentPeopleCnt; // 현재 인원

    @OneToMany(fetch = LAZY, mappedBy = "unit", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UnitUser> unitUsers = new ArrayList<>(); // 같은 유닛 인원

    public int assignUnitUser() {
        if (currentPeopleCnt <= 1) {
            currentPeopleCnt++;
        }
        return currentPeopleCnt;
    }


//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "unit_user_id", nullable = true)
//    private UnitUser unitUser; // 1개의 Unit 1개의 UnitUser에 속함
}
// 방 생성