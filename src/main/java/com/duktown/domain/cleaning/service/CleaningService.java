package com.duktown.domain.cleaning.service;

import com.duktown.domain.cleaning.dto.CleaningDto;
import com.duktown.domain.cleaning.entity.Cleaning;
import com.duktown.domain.cleaning.entity.CleaningRepository;
import com.duktown.domain.user.entity.User;
import com.duktown.domain.user.entity.UserRepository;
import com.duktown.global.exception.CustomErrorType;
import com.duktown.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
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

    // 날짜별 청소 조회
    public CleaningDto.CleaningDateResponseSto getCleanDate(CleaningDto.DateCleaningRequestDto date, Long userId){
        User user = userRepository.findById(userId)
                .orElseThrow(()->new CustomException(USER_NOT_FOUND));
//        Cleaning cleaningByDate = cleaningRepository.findCleaningByDate(date.getCleaningDate())
        Cleaning cleaningByDate = cleaningRepository.findCleaningByDateAndCheckUser(date.getCleaningDate(),user)
                .orElseThrow(()-> new CustomException(CLEANING_NOT_FOUND));
        return new CleaningDto.CleaningDateResponseSto(cleaningByDate);
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
}
