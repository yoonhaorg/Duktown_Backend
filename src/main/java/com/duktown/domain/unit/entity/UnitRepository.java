package com.duktown.domain.unit.entity;

import com.duktown.global.type.HallName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UnitRepository extends JpaRepository<Unit,Long> {
    @Query("SELECT u FROM Unit u WHERE u.floorNumber = :floorNumber AND u.buildingNumber = :buildingNumber AND u.roomNumber IS NULL")
    List<Unit> findEmptyUnits(Integer floorNumber, Integer buildingNumber);

    @Query("SELECT MAX(u.roomNumber) FROM Unit u WHERE u.hallName = :hallName AND u.floorNumber = :floorNumber AND u.buildingNumber = :buildingNumber")
    Integer getMaxRoomNumber(HallName hallName, Integer floorNumber, Integer buildingNumber);
}
