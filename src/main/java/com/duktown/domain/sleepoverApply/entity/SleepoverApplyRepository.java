package com.duktown.domain.sleepoverApply.entity;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SleepoverApplyRepository extends JpaRepository<SleepoverApply,Long> {
    Page<SleepoverApply> findAll(Pageable pageable);
}
