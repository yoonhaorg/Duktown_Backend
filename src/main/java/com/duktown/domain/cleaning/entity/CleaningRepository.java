package com.duktown.domain.cleaning.entity;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;

public interface CleaningRepository extends JpaRepository<Cleaning,Long> {
    Cleaning findCleaningById(Long cleaningId);
    Cleaning findCleaningByDate(LocalDate date);
}
