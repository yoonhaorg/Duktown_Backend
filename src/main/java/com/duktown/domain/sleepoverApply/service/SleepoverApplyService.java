package com.duktown.domain.sleepoverApply.service;

import com.duktown.domain.sleepoverApply.dto.SleepoverApplyDto;
import com.duktown.domain.sleepoverApply.entity.SleepoverApply;
import com.duktown.domain.sleepoverApply.entity.SleepoverApplyRepository;
import com.duktown.domain.user.entity.User;
import com.duktown.domain.user.entity.UserRepository;
import com.duktown.global.exception.CustomException;
import com.duktown.global.type.ApprovalType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static com.duktown.global.exception.CustomErrorType.*;

@Service
@RequiredArgsConstructor
@Transactional
public class SleepoverApplyService {
    private final SleepoverApplyRepository sleepoverApplyRepository;
    private final UserRepository userRepository;

    public void createSleepoverApply(Long userId, SleepoverApplyDto.RequestSleepoverApplyDto request){
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(USER_NOT_FOUND));


        // 외박 시작 날짜 + 현재 로직 시간을 기반으로 22시 체크
        if(processRequests(request.getStartDate())){
            SleepoverApply sleepoverApply = request.toEntity(user);
            sleepoverApplyRepository.save(sleepoverApply);
        }else {
            throw new CustomException(SLEEP_OVER_APPLY_INVALID_REQUEST_TIME);
        }

    }

    //TODO: 외박 신청 수정 -> 하루 추가-> 추가 신청-> 이전 내역과 병합

    // 외박 신청 수정 ->  하루 감소 => 기존 신청 이력을 수정
    public void updateSleepoverApply(Long userId,Long sleepoverApplyId ,SleepoverApplyDto.RequestSleepoverApplyDto requestUpdateDto){
        User user = userRepository.findById(userId)
                .orElseThrow(()-> new CustomException(USER_NOT_FOUND));

        SleepoverApply updateSleepover = sleepoverApplyRepository.findById(sleepoverApplyId)
                .orElseThrow(()-> new CustomException(SLEEP_OVER_APPLY_NOT_FOUND));
        SleepoverApply requestUpdate = requestUpdateDto.toEntity(user);

        if(updateSleepover.getApproved().equals(ApprovalType.Approved)) {
            throw new CustomException(SLEEP_OVER_APPLY_TARGET_ERROR);
        }else updateSleepover.updateSleepoverApply(requestUpdate.getStartDate(), requestUpdate.getEndDate(), requestUpdate.getPeriod(),requestUpdate.getAddress(), requestUpdate.getReason());

    }

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
    public SleepoverApplyDto.ResponseGetSleepoverApply getDetailSleepoverApply(Long sleepoverApplyId){
       SleepoverApply getSleepoverApply = sleepoverApplyRepository.findById(sleepoverApplyId)
               .orElseThrow(()-> new CustomException(SLEEP_OVER_APPLY_NOT_FOUND));
        return new SleepoverApplyDto.ResponseGetSleepoverApply(getSleepoverApply);
    }

    public void approveSleepoverApply(Long sleepoverApplyId){
            SleepoverApply sleepoverApply = sleepoverApplyRepository.findById(sleepoverApplyId)
                    .orElseThrow(()->new CustomException(SLEEP_OVER_APPLY_NOT_FOUND));
            sleepoverApply.approve(ApprovalType.Approved);

    }

    private boolean processRequests(LocalDate startDate) {
        LocalDateTime currentTime = LocalDateTime.now();
        // 외박 시작 날짜와 신청 날짜가 같으면서 신청 로직 currentTime이 22시 이후인 경우 외박 신청 거부
        if (startDate.isEqual(currentTime.toLocalDate()) && currentTime.getHour()>= 22) {
            return false;
        }
        return true;
    }


    }
