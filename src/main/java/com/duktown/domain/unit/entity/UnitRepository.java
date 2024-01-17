package com.duktown.domain.unit.entity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UnitRepository extends JpaRepository<Unit,Long> {

    //
//    Optional<Unit> findFirstOrderByCreatedAtDesc();


    // 인원 수가 작은 방부터 정렬하여 채우기
//    @Query("SELECT u FROM Unit u ORDER BY SIZE(u.users)")
//    List<Unit> findAllocationByOrderByOccupancy();
//
//    // 현재 방에 인원 수 확이하기
//    @Query("SELECT COUNT(u) FROM User u WHERE u.unit = :unit")
//    int countUsersByAllocation(@Param("unit") Optional<Unit> unit);


    // 유닛번호가 같은 유닛 조회
//    List<Unit> findByUnitNumber(Integer unitNumber);

    // 방이 있는지 확인
//    Boolean existsByRoomNumber(Integer roomNumber);


    Optional<Unit> findFirstByOrderByIdDesc();

    List<Unit> findFirst4ByOrderById();
}
