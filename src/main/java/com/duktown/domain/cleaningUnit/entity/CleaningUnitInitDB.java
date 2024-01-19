package com.duktown.domain.cleaningUnit.entity;

import com.duktown.domain.cleaning.entity.Cleaning;
import com.duktown.domain.cleaning.entity.CleaningRepository;
import com.duktown.domain.unitUser.entity.UnitUser;
import com.duktown.domain.unitUser.entity.UnitUserRepository;
import com.duktown.domain.user.entity.User;
import com.duktown.global.exception.CustomErrorType;
import com.duktown.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class CleaningUnitInitDB {
    //TODO: 배포를 위해 가상 유저 데이터에 청소를 임의로 배정합니다.

    private final UnitUserRepository unitUserRepository;
    private final CleaningRepository cleaningRepository;



    public void allocationCleaning(Long id) {
        // 기본 4개 유닛 정보 받아오기 (데모버전용)
        List<UnitUser> unitUsers = unitUserRepository.findFirst11ByOrderByCreatedAtAsc();
        UnitUser unitUser = unitUserRepository.findByUserId(id).orElseThrow(() -> new CustomException(CustomErrorType.UNIT_USER_NOT_FOUND));

        // 하나의 리스트로 합치기
        List<UnitUser> combinedList = new ArrayList<>(unitUsers);
        combinedList.add(unitUser);

        LocalDate starDate = LocalDate.now().with(TemporalAdjusters.firstDayOfMonth()); // 매 달 1일로 설정
        int lastDayOfMonth = LocalDate.now().with(TemporalAdjusters.lastDayOfMonth()).getDayOfMonth();   // 현재 날짜의 달의 마지막 날

        // combinedList의 인덱스 초기화
        int combinedListIndex = 0;

        for (int i = 1; i <= lastDayOfMonth; i++) {
            // 토요일, 일요일인 경우 다음 날로 넘어감
            if (starDate.getDayOfWeek() == DayOfWeek.SATURDAY || starDate.getDayOfWeek() == DayOfWeek.SUNDAY) {
                starDate = starDate.plusDays(1);
                continue;
            }

            // combinedList의 UnitUser에서 User 객체를 추출
            User user = combinedList.get(combinedListIndex).getUser();

            // Cleaning 객체 생성
            Cleaning cleaning = Cleaning.builder()
                    .date(starDate)  // 날짜 설정
                    .user(user)
                    .checkUser(unitUser.getUser())
                    .cleaned(false)
                    .checked(false)
                    .build();

            // 다음 날로 설정
            starDate = starDate.plusDays(1);

            // combinedList의 인덱스 증가
            combinedListIndex = (combinedListIndex + 1) % combinedList.size();

            // 예시: cleaning 저장
            cleaningRepository.save(cleaning);
        }
    }

}
