package com.duktown.domain.myPage.service;

import com.duktown.domain.penaltyPoints.entity.PenaltyPointsRepository;
import com.duktown.domain.user.entity.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MyPageService {
    private final PenaltyPointsRepository penaltyPointsRepository;
    private final UserRepository userRepository;

    // 나의 벌점 내역 조회

    // 나의 유닛 조회
}
