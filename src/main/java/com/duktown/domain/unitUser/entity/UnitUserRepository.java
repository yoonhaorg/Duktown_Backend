package com.duktown.domain.unitUser.entity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UnitUserRepository extends JpaRepository<UnitUser,Long> {

    Optional<UnitUser> findByUserId(Long userId);
    List<UnitUser> findFirst11ByOrderByCreatedAtAsc();

}
