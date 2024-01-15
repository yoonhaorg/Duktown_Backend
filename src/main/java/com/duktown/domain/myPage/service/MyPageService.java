package com.duktown.domain.myPage.service;

import com.duktown.domain.myPage.dto.PenaltyPointsDto;
import com.duktown.domain.penaltyPoints.entity.PenaltyPoints;
import com.duktown.domain.penaltyPoints.entity.PenaltyPointsRepository;
import com.duktown.domain.user.entity.User;
import com.duktown.domain.user.entity.UserRepository;
import com.duktown.global.exception.CustomErrorType;
import com.duktown.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MyPageService {
    private final PenaltyPointsRepository penaltyPointsRepository;
    private final UserRepository userRepository;

    // 나의 벌점 내역 조회
    public PenaltyPointsDto.PenaltyPointsListResponseDto getMyPenaltyPoints(Long userId){
        User user = userRepository.findById(userId)
                .orElseThrow(()-> new CustomException(CustomErrorType.USER_NOT_FOUND));

        List<PenaltyPoints> penaltyPoints = penaltyPointsRepository.findPenaltyPointsByUser(user);
        List<PenaltyPointsDto.PenaltyPointsResponseDto> penaltyPointsResponseDto
                = penaltyPoints.stream()
                .map(PenaltyPointsDto.PenaltyPointsResponseDto::new)
                .collect(Collectors.toList());

        // 총합 벌점을 쿼리문으로 계산하여 반환
        Long totalPenaltyPointsByUser = penaltyPointsRepository.getTotalPenaltyPointsByUser(user);
        return new PenaltyPointsDto.PenaltyPointsListResponseDto(penaltyPointsResponseDto,totalPenaltyPointsByUser);
    }

    // 나의 유닛 조회
    public void getMyUnits(){

    }
}
