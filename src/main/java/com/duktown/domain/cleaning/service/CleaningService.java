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

import java.time.LocalDate;
import java.util.Optional;

import static com.duktown.global.exception.CustomErrorType.USER_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class CleaningService {

    private final CleaningRepository cleaningRepository;
    private final UserRepository userRepository;

    // 날짜별 청소 조회
    public CleaningDto.GetCleaningDate getCleanDate(LocalDate date){
        Cleaning cleaningByDate = cleaningRepository.findCleaningByDate(date);
        return new CleaningDto.GetCleaningDate(cleaningByDate);
    }


    // 청소 완료
    public void CheckCleaning(Long cleaningId){
        Cleaning cleaning = cleaningRepository.findCleaningById(cleaningId);
        cleaning.updateCleaned();
    }

    // 청소 승인
    public void cleaningApply(Long cleaningId){
        Cleaning cleaning = cleaningRepository.findCleaningById(cleaningId);
        cleaning.updateCleaned();
    }
    //TODO: 벌점 부여 : 청소 승인 과정에서 벌점 부여


    // 유닛 조장이 청소 날짜 신청
    //TODO: 기획에 따라 리스트로 신청입력, 개별 신청 입력 확인하기
    public void createCleaningDate(CleaningDto.CreateCleaningDto createCleaningDto){
        User user = userRepository.findByEmail(createCleaningDto.getEmail())
                .orElseThrow(()->new CustomException(USER_NOT_FOUND));

        Cleaning cleaning = createCleaningDto.toEntity(user);
        cleaningRepository.save(cleaning);
    }
}
