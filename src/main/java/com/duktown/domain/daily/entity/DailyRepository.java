package com.duktown.domain.daily.entity;

import org.springframework.data.jpa.repository.JpaRepository;

public interface DailyRepository extends JpaRepository<Daily,Long> {
    // 목록 조회 - 페이지네이션, 카테고리
}
