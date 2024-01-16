package com.duktown.domain.unit.service;

import com.duktown.domain.unit.entity.UnitRepository;
import com.duktown.domain.unit.entity.Unit;
import com.duktown.domain.user.entity.User;
import com.duktown.domain.user.entity.UserRepository;
import com.duktown.global.exception.CustomErrorType;
import com.duktown.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Iterator;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UnitService {

    private final UnitRepository unitRepository;
    private final UserRepository userRepository;


    @Transactional
    public void assignRoom(Long userId) {
        List<Unit> units = unitRepository.findAll();

        Iterator<Unit> iterator = units.iterator();

        while (iterator.hasNext()) {
            Unit unit = iterator.next();
            Optional<Unit> optionalUnit = Optional.of(unit);
            int occupantsCount = unitRepository.countUsersByAllocation(optionalUnit);
            int maxOccupancy = unit.getOccupancy();

            if (occupantsCount < maxOccupancy) {
                assignUserToAllocation(userId, unit);
                System.out.println(userId + "님, " + unit.getRoomNumber() + "번 방에 배정되셨습니다.");
                return; // 첫 번째로 발견한 비어 있는 방에만 배정하고 메서드를 종료합니다.
            }
        }

        // 모든 방이 차있는 경우에 새로운 방을 생성하여 배정합니다.
        int nextRoomNumber = calculateNextRoomNumber(units.size());
        Unit newUnit = createNewAllocation(nextRoomNumber, 2);
        assignUserToAllocation(userId, newUnit);
        System.out.println("모든 방이 다 찼습니다. 새로운 방을 만들었습니다.");
    }


    public Unit createNewAllocation(int number, int maxOccupancy) {
        Unit newUnit = new Unit();
        newUnit.setRoomNumber(number);
        newUnit.setOccupancy(maxOccupancy);
        return unitRepository.save(newUnit);
    }

    private void assignUserToAllocation(Long userId, Unit unit) {
        User user = userRepository.findById(userId)
                .orElseThrow(()-> new CustomException(CustomErrorType.USER_NOT_FOUND));
        user.toAllocation(unit);
        userRepository.save(user);
    }

    // 방 번호 계산 로직 추가
    private int calculateNextRoomNumber(int currentSize) {
        int baseNumber = 100;
        int step = 100;

        int nextRoomNumber = baseNumber + (currentSize % step) * step;

        // 8로 끝나면 100을 더해줌
        if (nextRoomNumber % 10 == 8) {
            nextRoomNumber += 100;
        }

        return nextRoomNumber;
    }
}
