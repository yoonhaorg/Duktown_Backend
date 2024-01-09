package com.duktown.domain.sleepoverApply.entity;

import com.duktown.domain.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SleepoverApplyRepository extends JpaRepository<SleepoverApply,Long> {
    Page<SleepoverApply> findAll(Pageable pageable);

    Slice<SleepoverApply> findByUser(User user, Pageable pageable);

    SleepoverApply findByUser(User user);
}
