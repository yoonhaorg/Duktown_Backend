package com.duktown.domain.unit.entity;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UnitRepository extends JpaRepository<Unit,Long> {

    Optional<Unit> findFirstByOrderByIdDesc();

    List<Unit> findFirst4ByOrderById();
}
