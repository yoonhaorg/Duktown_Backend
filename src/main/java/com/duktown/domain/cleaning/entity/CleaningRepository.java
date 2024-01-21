package com.duktown.domain.cleaning.entity;

import com.duktown.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface CleaningRepository extends JpaRepository<Cleaning,Long> {
    Optional<Cleaning> findCleaningById(Long cleaningId);

    List<Cleaning> findCleaningByUser(User user);

    //TODO: 유닛 내 날짜별 청소조회 -> 배포용
    @Query("SELECT c FROM Cleaning c WHERE c.date BETWEEN :startDate AND :endDate AND c.checkUser = :user")
    List<Cleaning> findCleaningByDateAndCheckUserBetween(@Param("user") User user,
                                                         @Param("startDate") LocalDate startDate,
                                                         @Param("endDate") LocalDate endDate);

    // 유닛 내 날짜 조회
   // Optional<Cleaning> findCleaningByDateBetween(LocalDate startDate,LocalDate endDAte ,User user);

    // 유저 완료한 청소 내역 조회
    List<Cleaning> findCleaningByUserAndCleaned(User user, Boolean cleaned);
}
