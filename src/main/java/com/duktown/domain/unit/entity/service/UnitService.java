package com.duktown.domain.unit.entity.service;

import com.duktown.domain.unit.entity.Unit;
import com.duktown.domain.unit.entity.UnitRepository;
import com.duktown.global.type.HallName;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UnitService {

    private final UnitRepository unitRepository;

    @Transactional
    public Unit assignUnitToUser() {
        //TODO: 3층, 1동에 새로운 방을 배정 -> 이걸 자동으로
        HallName hallName = HallName.GAON1;
        Integer floorNumber = 3;
        Integer buildingNumber = 1;

        // 빈 방을 찾거나 새로운 방을 생성
        Unit assignedUnit = findOrAssignUnit(hallName, floorNumber, buildingNumber);

        return assignedUnit;
    }

    private Unit findOrAssignUnit(HallName hallName, Integer floorNumber, Integer buildingNumber) {
        // 빈 방을 찾음
        Unit emptyUnit = findEmptyUnit(floorNumber, buildingNumber);
        if (emptyUnit != null) {
            return emptyUnit;
        }

        // 빈 방이 없을 경우, 새로운 방 생성
        return createNewUnit(hallName, floorNumber, buildingNumber);
    }

    private Unit findEmptyUnit(Integer floorNumber, Integer buildingNumber) {
        // 빈 방을 조회
        List<Unit> emptyUnits = unitRepository.findEmptyUnits(floorNumber, buildingNumber);
        if (!emptyUnits.isEmpty()) {
            return emptyUnits.get(0);
        }
        return null;
    }

    @Transactional
    public Unit createNewUnit(HallName hallName, Integer floorNumber, Integer buildingNumber) {
        // 새로운 방 생성
        Integer nextRoomNumber = unitRepository.getMaxRoomNumber(hallName, floorNumber, buildingNumber) + 1;

        // 한 방에 2명씩 배정되도록 변경
        int roomNumberOffset = nextRoomNumber % 2 == 0 ? 0 : 1;
        nextRoomNumber += roomNumberOffset;

        return Unit.builder()
                .hallName(hallName)
                .floorNumber(floorNumber)
                .buildingNumber(buildingNumber)
                .roomNumber(nextRoomNumber)
                .build();
    }

}
