package com.duktown.domain.unit.entity;

import com.duktown.domain.user.entity.User;
import com.duktown.global.type.HallName;
import lombok.*;

import javax.persistence.*;

import static javax.persistence.EnumType.STRING;
import static javax.persistence.FetchType.*;
import static lombok.AccessLevel.*;

@Entity
@Getter
@Builder
@AllArgsConstructor(access = PRIVATE)
@NoArgsConstructor(access = PROTECTED)
public class Unit {
    @Id
    @GeneratedValue
    @Column(name = "unit_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated(value = STRING)
    @Column(nullable = false)
    private HallName hallName;

    @Column(nullable = false)
    private Integer floorNumber;

    @Column(nullable = false)
    private Integer buildingNumber;

    @Column(nullable = false)
    private Integer roomNumber;
}