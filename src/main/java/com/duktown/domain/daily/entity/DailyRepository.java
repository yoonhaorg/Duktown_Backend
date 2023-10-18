package com.duktown.domain.daily.entity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DailyRepository extends JpaRepository<Daily,Long> {
    // 목록 조회 - 페이지네이션, 카테고리
}
