package com.duktown.domain.unit.entity;

import com.duktown.domain.user.entity.User;
import com.duktown.global.type.HallName;
import lombok.*;

import javax.persistence.*;

import static javax.persistence.EnumType.STRING;
import static javax.persistence.FetchType.*;
import static javax.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.*;

@Entity
@Getter
@Builder
@AllArgsConstructor(access = PRIVATE)
@NoArgsConstructor(access = PROTECTED)
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
}
