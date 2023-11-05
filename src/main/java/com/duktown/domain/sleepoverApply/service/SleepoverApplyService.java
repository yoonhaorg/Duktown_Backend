package com.duktown.domain.sleepoverApply.service;

import com.duktown.domain.sleepoverApply.dto.SleepoverApplyDto;
import com.duktown.domain.sleepoverApply.entity.SleepoverApply;
import com.duktown.domain.sleepoverApply.entity.SleepoverApplyRepository;
import com.duktown.domain.user.entity.User;
import com.duktown.domain.user.entity.UserRepository;
import com.duktown.global.exception.CustomException;
import com.duktown.global.type.RoleType;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.duktown.global.exception.CustomErrorType.SLEEP_OVER_APPLY_NOT_FOUND;
import static com.duktown.global.exception.CustomErrorType.USER_NOT_FOUND;

@Service
@RequiredArgsConstructor
@Transactional
public class SleepoverApplyService {
    private final SleepoverApplyRepository sleepoverApplyRepository;
    private final UserRepository userRepository;

    public void createSleepoverApply(Long userId, SleepoverApplyDto.RequestSleepoverApplyDto request){
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(USER_NOT_FOUND));
         SleepoverApply sleepoverApply = request.toEntity(user);

         // 생성시간이전 요청시간 확인 로직 필요

         sleepoverApplyRepository.save(sleepoverApply);
    }

    // 외박 신청 수정 ->  하루 감소 => 기존 신청 이력을 수정
    // 외박 신청 수정 -> 하루 추가-> 추가 신청 -> 이전 내역과 병합

    public void deleteSleepoverApply(Long sleepoverApplyId , Long userId){
        userRepository.findById(userId).orElseThrow(()->new CustomException(USER_NOT_FOUND));
        SleepoverApply sleepoverApply = sleepoverApplyRepository.findById(sleepoverApplyId)
                .orElseThrow( ()->new CustomException(SLEEP_OVER_APPLY_NOT_FOUND));
        sleepoverApplyRepository.delete(sleepoverApply);
    }


    @Transactional(readOnly = true)
    public SleepoverApplyDto.ResponseGetListSleepoverApply getListSleepoverApply(){
        List<SleepoverApply> sleepoverApplies = sleepoverApplyRepository.findAll();

        List<SleepoverApplyDto.ResponseGetSleepoverApply> getSleepoverApplyList
                = sleepoverApplies.stream()
                .map(SleepoverApplyDto.ResponseGetSleepoverApply::new)
                .collect(Collectors.toList());

        return SleepoverApplyDto.ResponseGetListSleepoverApply.from(getSleepoverApplyList);
    }

    @Transactional(readOnly = true)
    public SleepoverApplyDto.ResponseGetSleepoverApply getSleepoverApply(Long sleepoverApplyId){
       SleepoverApply getSleepoverApply = sleepoverApplyRepository.findById(sleepoverApplyId).get();
        return new SleepoverApplyDto.ResponseGetSleepoverApply(getSleepoverApply);
    }

    public void approveSleepoverApply(Long sleepoverApplyId){
            SleepoverApply sleepoverApply = sleepoverApplyRepository.findById(sleepoverApplyId)
                    .orElseThrow(()->new CustomException(SLEEP_OVER_APPLY_NOT_FOUND));
            sleepoverApply.approve(true);

    }



}
