package com.duktown.domain.cleaning.entity;

import com.duktown.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface CleaningRepository extends JpaRepository<Cleaning,Long> {
    Cleaning findCleaningById(Long cleaningId);

    // 유닛 내 날짜별 청소조회
    Cleaning findCleaningByDate(LocalDate date);

    // 유저 완료한 청소 내역 조회
    List<Cleaning> findCleaningByUserAndCleaned(User user, Boolean cleaned);
}
