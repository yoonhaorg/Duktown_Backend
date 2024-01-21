package com.duktown.domain.cleaning.service;

import com.duktown.domain.cleaning.dto.CleaningDto;
import com.duktown.domain.cleaning.entity.Cleaning;
import com.duktown.domain.cleaning.entity.CleaningRepository;
import com.duktown.domain.delivery.dto.DeliveryDto;
import com.duktown.domain.unit.entity.UnitRepository;
import com.duktown.domain.unitUser.entity.UnitUser;
import com.duktown.domain.unitUser.entity.UnitUserRepository;
import com.duktown.domain.user.entity.User;
import com.duktown.domain.user.entity.UserRepository;
import com.duktown.global.exception.CustomErrorType;
import com.duktown.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.duktown.global.exception.CustomErrorType.CLEANING_NOT_FOUND;
import static com.duktown.global.exception.CustomErrorType.USER_NOT_FOUND;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CleaningService {

    private final CleaningRepository cleaningRepository;
    private final UserRepository userRepository;

    private final UnitUserRepository unitUserRepository;

    // 기간별 청소 조회
    public CleaningDto.ListDto getCleanDate(CleaningDto.DateCleaningRequestDto date, Long userId){
        User user = userRepository.findById(userId)
                .orElseThrow(()->new CustomException(USER_NOT_FOUND));
        // 배포용
        List<Cleaning> cleaningBetweenDate = cleaningRepository.findCleaningByDateAndCheckUserBetween(user,date.getStartDate(), date.getEndDate());
        List<CleaningDto.CleaningDateResponseDto> cleaningDateResponse = cleaningBetweenDate.stream()
                .map(CleaningDto.CleaningDateResponseDto::new).collect(Collectors.toList());
        return new CleaningDto.ListDto(cleaningDateResponse);
    }


    // 청소 완료
    @Transactional
    public void cleaningOk(Long cleaningId){
        Cleaning cleaning = cleaningRepository.findCleaningById(cleaningId)
                        .orElseThrow(()-> new CustomException(CLEANING_NOT_FOUND));
        cleaning.updateCleaned();
    }

    // 청소 승인
    //TODO: 벌점 부여 : 청소 승인 과정에서 벌점 부여
    @Transactional
    public void checkOk(Long cleaningId){
        Cleaning cleaning = cleaningRepository.findCleaningById(cleaningId)
                .orElseThrow(()-> new CustomException(CLEANING_NOT_FOUND));
        cleaning.updateChecked();
    }

    // 나의 청소 완료 목록 조회
    public CleaningDto.getCleaningListResponseDto getMyCleanedList(Long userId){
        User user = userRepository.findById(userId)
                        .orElseThrow(()->new CustomException(USER_NOT_FOUND));
        List<Cleaning> cleaningByUserAndCleaned = cleaningRepository.findCleaningByUserAndCleaned(user, true);
        List<CleaningDto.CleaningResponseDto> cleaningList = cleaningByUserAndCleaned.stream()
                .map(CleaningDto.CleaningResponseDto::new)
                .collect(Collectors.toList());
        return new CleaningDto.getCleaningListResponseDto(cleaningList);
    }


    // 유닛 조장이 청소 날짜 신청 //TODO: 개별, 단체 신청 확인하기
    @Transactional
    public void createCleaningDate(CleaningDto.CreateCleaningRequestDto createCleaningDto) {
//        List<CleaningDto.CreateCleaningRequestDto.CreateCleaningUnit> cleaningUnits = createCleaningDto.getCleaningUnit();
//
//        for (CleaningDto.CreateCleaningRequestDto.CreateCleaningUnit cleaningUnit : cleaningUnits) {
//            String email = cleaningUnit.getEmail();
//            User user = userRepository.findByEmail(email)
//                    .orElseThrow(() -> new CustomException(USER_NOT_FOUND));
//
//            Cleaning cleaning = cleaningUnit.toEntity(user);
//            cleaningRepository.save(cleaning);
//        }

            String email = createCleaningDto.getEmail();
            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new CustomException(USER_NOT_FOUND));

            Cleaning cleaning = createCleaningDto.toEntity(user);
            cleaningRepository.save(cleaning);
    }

    //청소 유닛 조회
    public CleaningDto.unitCleaningResponseDto myUnit(Long userId){
        // 기본 4개 유닛 정보 받아오기 (데모버전용)
        List<UnitUser> unitUsers = unitUserRepository.findFirst11ByOrderByCreatedAtAsc();
        UnitUser unitUser = unitUserRepository.findByUserId(userId).orElseThrow(() -> new CustomException(CustomErrorType.UNIT_USER_NOT_FOUND));

        // 하나의 리스트로 합치기
        List<UnitUser> combinedList = new ArrayList<>(unitUsers);
        combinedList.add(unitUser);
        List<CleaningDto.unitCleaningResponse> unitCleaning = combinedList.stream()
                .map(CleaningDto.unitCleaningResponse::new)
                .collect(Collectors.toList());
        return new CleaningDto.unitCleaningResponseDto(unitCleaning);

    }

    // 사생별 청소 일정 조회
    public CleaningDto.UserCleaningScheduleResponseDto StudentSchedule(Long studentId){
        User user = userRepository.findById(studentId)
                .orElseThrow(()->new CustomException(USER_NOT_FOUND));
        List<Cleaning> cleaningByUser = cleaningRepository.findCleaningByUser(user);
        List<CleaningDto.getUserCleaningSchedule> schedules = cleaningByUser.stream()
                .map(c -> new CleaningDto.getUserCleaningSchedule(c.getDate())).collect(Collectors.toList());
        return new CleaningDto.UserCleaningScheduleResponseDto(schedules);
    }
}
