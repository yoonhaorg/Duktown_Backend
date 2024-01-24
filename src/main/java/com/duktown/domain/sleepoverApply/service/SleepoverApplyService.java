package com.duktown.domain.sleepoverApply.service;

import com.duktown.domain.sleepoverApply.dto.SleepoverApplyDto;
import com.duktown.domain.sleepoverApply.entity.SleepoverApply;
import com.duktown.domain.sleepoverApply.entity.SleepoverApplyRepository;
import com.duktown.domain.user.entity.User;
import com.duktown.domain.user.entity.UserRepository;
import com.duktown.global.exception.CustomErrorType;
import com.duktown.global.exception.CustomException;
import com.duktown.global.type.ApprovalType;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static com.duktown.global.exception.CustomErrorType.*;
import static org.springframework.data.domain.Sort.Order.desc;

@Service
@RequiredArgsConstructor
@Transactional
public class SleepoverApplyService {
    private final SleepoverApplyRepository sleepoverApplyRepository;
    private final UserRepository userRepository;

    public void createSleepoverApply(Long userId, SleepoverApplyDto.RequestSleepoverApplyDto request){
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(USER_NOT_FOUND));


        //외박 시작 날짜 + 현재 로직 시간을 기반으로 22시 체크
        if(processRequests(request.getStartDate())){
            long remainingDays = calculateRemainingDaysExcludingWeekends(request.getStartDate(), request.getEndDate());

            request.setPeriod(remainingDays > 0 ? Math.toIntExact(remainingDays) : 0); // 실제 외박 일수를 DTO에 추가

            SleepoverApply sleepoverApply = request.toEntity(user);

            // TODO : 외박 가능 일 수가 0이면 에러 처리 발생
            if(user.getAvailablePeriod().equals(0) && user.getAvailablePeriod() < remainingDays){
                throw new CustomException(CustomErrorType.SLEEP_OVER_APPLY_TOTAL_ERROR);
            }
            user.downAvailablePeriod(request.getPeriod());// 외박 가능 일 수 감소
            sleepoverApplyRepository.save(sleepoverApply);

        }else {
            throw new CustomException(SLEEP_OVER_APPLY_INVALID_REQUEST_TIME);
        }

    }

    private boolean processRequests(LocalDate startDate) {
        LocalDateTime currentTime = LocalDateTime.now();
        // 외박 시작 날짜와 신청 날짜가 같으면서 신청 로직 currentTime이 22시 이후인 경우 외박 신청 거부
        if (startDate.isEqual(currentTime.toLocalDate()) && currentTime.getHour()>= 22) {
            return false;
        }
        return true;
    }

    // 외박 시작 신청 시작일 종료일 계산 내부 메서드
    public long calculateRemainingDaysExcludingWeekends(LocalDate startDate, LocalDate endDate) {
        long remainingDays = 0;

        // 시작일과 종료일이 같은 경우도 고려
        if (startDate.isEqual(endDate) &&
                startDate.getDayOfWeek() != DayOfWeek.FRIDAY &&
                startDate.getDayOfWeek() != DayOfWeek.SATURDAY &&
                startDate.getDayOfWeek() != DayOfWeek.SUNDAY) {
            return 1;
        } else if (startDate.isEqual(endDate) && (startDate.getDayOfWeek() == DayOfWeek.FRIDAY ||
                startDate.getDayOfWeek() == DayOfWeek.SATURDAY ||
                startDate.getDayOfWeek() == DayOfWeek.SUNDAY)) {
            return 0;
        }

        LocalDate currentDate = startDate;
        while (!currentDate.isAfter(endDate.minusDays(1))) {
            // 금,토,일이 아니면 카운트
            if (currentDate.getDayOfWeek() != DayOfWeek.FRIDAY &&
                    currentDate.getDayOfWeek() != DayOfWeek.SATURDAY &&
                    currentDate.getDayOfWeek() != DayOfWeek.SUNDAY) {
                remainingDays++;
            }

            // 다음 날짜로 이동
            currentDate = currentDate.plusDays(1);

        }

        return remainingDays;
    }


    public void deleteSleepoverApply(Long sleepoverApplyId , Long userId){
        userRepository.findById(userId).orElseThrow(()->new CustomException(USER_NOT_FOUND));
        SleepoverApply sleepoverApply = sleepoverApplyRepository.findById(sleepoverApplyId)
                .orElseThrow( ()->new CustomException(SLEEP_OVER_APPLY_NOT_FOUND));
        sleepoverApplyRepository.delete(sleepoverApply);
    }

    @Transactional(readOnly = true)
    public SleepoverApplyDto.ResponseGetSleepoverApplyFromManager getListSleepoverApply(int pageNo){
        Page<SleepoverApply> sleepoverApplies = sleepoverApplyRepository.findAll(PageRequest.of(pageNo-1,5, Sort.by(desc("createdAt"),Sort.Order.asc("approved"))));

        List<SleepoverApplyDto.ResponseGetSleepoverApply> getSleepoverApplyList
                = sleepoverApplies.stream()
                .map(SleepoverApplyDto.ResponseGetSleepoverApply::new)
                .collect(Collectors.toList());

        // 현재 페이지가 첫 페이지인지, 마지막 페이지인지에 대한 정보를 추출하여 DTO에 추가
        boolean isFirstPage = sleepoverApplies.isFirst();
        boolean isLastPage = sleepoverApplies.isLast();

        return SleepoverApplyDto.ResponseGetSleepoverApplyFromManager.from(getSleepoverApplyList, isFirstPage, isLastPage);
    }

    // 목록 조회
    @Transactional(readOnly = true)
    public SleepoverApplyDto.ResponseGetSleepoverApplyFromStudent getListSleepoverApply(Long userId, int pageNo){
        User user = userRepository.findById(userId).orElseThrow(() -> new CustomException(USER_NOT_FOUND));
        Slice<SleepoverApply> sleepoverApplies = sleepoverApplyRepository.findByUser(user, PageRequest.of(pageNo-1, 5, Sort.by(Sort.Order.desc("createdAt"))));
        List<SleepoverApplyDto.ResponseGetListSleepoverApply> getListSleepoverApplies
                = sleepoverApplies.stream()
                .map(SleepoverApplyDto.ResponseGetListSleepoverApply::new)
                .collect(Collectors.toList());

        return SleepoverApplyDto.ResponseGetSleepoverApplyFromStudent.from(getListSleepoverApplies, totalAvailablePeriod(userId).getAvailablePeriod());
    }

    //외박 가능 횟수 조회
    @Transactional(readOnly = true)
    public SleepoverApplyDto.ResponseGetAvailablePeriod totalAvailablePeriod(Long userId){
        User user = userRepository.findById(userId).orElseThrow(() -> new CustomException(USER_NOT_FOUND));
        Integer availablePeriod = user.getAvailablePeriod();
        return  new SleepoverApplyDto.ResponseGetAvailablePeriod(availablePeriod);
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



}
