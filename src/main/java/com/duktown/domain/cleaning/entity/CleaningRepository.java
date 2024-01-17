package com.duktown.domain.cleaning.entity;

import com.duktown.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface CleaningRepository extends JpaRepository<Cleaning,Long> {
    Optional<Cleaning> findCleaningById(Long cleaningId);

    // 유닛 내 날짜별 청소조회
    Optional<Cleaning> findCleaningByDate(LocalDate date);

    // 유저 완료한 청소 내역 조회
    List<Cleaning> findCleaningByUserAndCleaned(User user, Boolean cleaned);
}
