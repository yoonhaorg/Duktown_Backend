package com.duktown.domain.unitUser.entity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UnitUserRepository extends JpaRepository<UnitUser,Long> {

    // 방 배정을 위해 조회하는
//    //Optional<UnitUser> findFirstOrderByCreatedAtDesc();

    Optional<UnitUser> findByUserId(Long userId);

}
