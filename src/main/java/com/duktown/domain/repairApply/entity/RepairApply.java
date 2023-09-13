package com.duktown.domain.repairApply.entity;

import com.duktown.domain.user.entity.User;
import com.duktown.global.type.HallName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import static javax.persistence.EnumType.STRING;
import static javax.persistence.FetchType.LAZY;
import static lombok.AccessLevel.PRIVATE;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@Builder
@AllArgsConstructor(access = PRIVATE)
@NoArgsConstructor(access = PROTECTED)
@Table(name = "repair_apply")
public class RepairApply {
    @Id
    @GeneratedValue
    @Column(name = "repair_apply_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    @Enumerated(value = STRING)
    private HallName hallName;

    private String unit;

    @Column(columnDefinition = "longtext", nullable = false)
    private String content;

    private Boolean checked;

    private Boolean solved;
}
