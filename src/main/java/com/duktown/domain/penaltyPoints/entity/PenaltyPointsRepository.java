package com.duktown.domain.penaltyPoints.entity;

import com.duktown.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PenaltyPointsRepository extends JpaRepository<PenaltyPoints,Long> {
    // 내 벌점 내역 조회
    List<PenaltyPoints> findPenaltyPointsByUserOrderByDateDesc(User user);
    // 벌점 점수 총합
    @Query("SELECT SUM(pp.score) FROM PenaltyPoints pp WHERE pp.user = :user")
    Long getTotalPenaltyPointsByUser(@Param("user") User user);

// 배포용
    @Query("SELECT SUM(pp.score) FROM PenaltyPoints pp")
    Long getTotalPenaltyPoints();
    // 전체 벌점 내역 조회
    List<PenaltyPoints> findAllByOrderByDateDesc();




}
