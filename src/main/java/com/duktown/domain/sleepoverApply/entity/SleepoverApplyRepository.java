package com.duktown.domain.sleepoverApply.entity;

import com.duktown.domain.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;

public interface SleepoverApplyRepository extends JpaRepository<SleepoverApply,Long> {
    Page<SleepoverApply> findAll(Pageable pageable);

    Slice<SleepoverApply> findByUser(User user, Pageable pageable);

    @Query("SELECT COUNT(sa) > 0 FROM SleepoverApply sa " +
            "WHERE sa.user = :user " +
            "AND ((sa.startDate < :endDate AND sa.endDate > :startDate) " +
            "OR (sa.startDate < :startDate AND sa.endDate > :startDate))")
    boolean OverlappingSleepover( //이력이 존재하면 true, 그렇지 않으면 false
            @Param("user") User user, @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

}
