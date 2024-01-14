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

import static com.duktown.global.exception.CustomErrorType.USER_NOT_FOUND;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CleaningService {

    private final CleaningRepository cleaningRepository;
    private final UserRepository userRepository;

    // 날짜별 청소 조회
    public CleaningDto.GetCleaningDate getCleanDate(LocalDate date){
        Cleaning cleaningByDate = cleaningRepository.findCleaningByDate(date);
        return new CleaningDto.GetCleaningDate(cleaningByDate);
    }

    // 청소 완료
    @Transactional
    public void CheckCleaning(Long cleaningId){
        Cleaning cleaning = cleaningRepository.findCleaningById(cleaningId);
        cleaning.updateCleaned();
    }

    // 청소 승인
    //TODO: 벌점 부여 : 청소 승인 과정에서 벌점 부여
    @Transactional
    public void cleaningApply(Long cleaningId){
        Cleaning cleaning = cleaningRepository.findCleaningById(cleaningId);
        cleaning.updateCleaned();
    }

    // 나의 청소 완료 목록 조회


    // 유닛 조장이 청소 날짜 신청
    //TODO: 개별, 단체 신청 확인하기
    @Transactional
    public void createCleaningDate(CleaningDto.CreateCleaningDto createCleaningDto) {
        List<CleaningDto.CreateCleaningDto.CreateCleaningUnit> cleaningUnits = createCleaningDto.getCleaningUnit();

        for (CleaningDto.CreateCleaningDto.CreateCleaningUnit cleaningUnit : cleaningUnits) {
            String email = cleaningUnit.getEmail();
            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new CustomException(USER_NOT_FOUND));

            Cleaning cleaning = cleaningUnit.toEntity(user);
            cleaningRepository.save(cleaning);
        }
    }
}
