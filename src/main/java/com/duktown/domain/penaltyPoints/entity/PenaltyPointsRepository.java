package com.duktown.domain.penaltyPoints.entity;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PenaltyPointsRepository extends JpaRepository<PenaltyPoints,Long> {
    // 내 벌점 내역 조회

}
