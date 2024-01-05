package com.duktown.domain.sleepoverApply.service;

import com.duktown.domain.sleepoverApply.dto.SleepoverApplyDto;
import com.duktown.domain.sleepoverApply.entity.SleepoverApply;
import com.duktown.domain.sleepoverApply.entity.SleepoverApplyRepository;
import com.duktown.domain.user.entity.User;
import com.duktown.domain.user.entity.UserRepository;
import com.duktown.global.exception.CustomException;
import com.duktown.global.type.ApprovalType;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
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


        //외박 시작 날짜 + 현재 로직 시간을 기반으로 22시 체크
        if(processRequests(request.getStartDate())){
            //TODO: 남은 외박 일수 필요함 +  금,토,일은 외박 날짜에서 제외함.
            long remainingDays = calculateRemainingDays(request.getStartDate(), request.getEndDate());

            // 남은 외박 일수를 로그로 출력
            System.out.println("Remaining days: " + remainingDays);

            request.setPeriod(remainingDays > 0 ? Math.toIntExact(remainingDays) : 0);

            SleepoverApply sleepoverApply = request.toEntity(user);
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

    public long calculateRemainingDays(LocalDate startDate, LocalDate endDate) {
        // 시작 날짜부터 종료 날짜까지의 날짜 차이 계산
        long daysBetween = ChronoUnit.DAYS.between(startDate, endDate);

        // 금,토,일을 제외하고 남은 외박 일수 계산
        long remainingDays = daysBetween - countWeekendDays(startDate, endDate);

        return remainingDays > 0 ? remainingDays : 0;
    }

    private long countWeekendDays(LocalDate startDate, LocalDate endDate) {
        long weekendDays = 0;

        LocalDate currentDate = startDate;
        while (!currentDate.isAfter(endDate)) {
            // 금,토,일이면 제외
            if (currentDate.getDayOfWeek() == DayOfWeek.FRIDAY ||
                    currentDate.getDayOfWeek() == DayOfWeek.SATURDAY ||
                    currentDate.getDayOfWeek() == DayOfWeek.SUNDAY) {
                weekendDays++;
            }

            // 다음 날짜로 이동
            currentDate = currentDate.plusDays(1);
        }

        return weekendDays;
    }

    public void deleteSleepoverApply(Long sleepoverApplyId , Long userId){
        userRepository.findById(userId).orElseThrow(()->new CustomException(USER_NOT_FOUND));
        SleepoverApply sleepoverApply = sleepoverApplyRepository.findById(sleepoverApplyId)
                .orElseThrow( ()->new CustomException(SLEEP_OVER_APPLY_NOT_FOUND));
        sleepoverApplyRepository.delete(sleepoverApply);
    }


    @Transactional(readOnly = true)
    public SleepoverApplyDto.ResponseGetListSleepoverApply getListSleepoverApply(int pageNo){
        Page<SleepoverApply> sleepoverApplies = sleepoverApplyRepository.findAll(PageRequest.of(pageNo,pageNo, Sort.by(Sort.Order.desc("createdAt"),Sort.Order.asc("approved"))));

        List<SleepoverApplyDto.ResponseGetSleepoverApply> getSleepoverApplyList
                = sleepoverApplies.stream()
                .map(SleepoverApplyDto.ResponseGetSleepoverApply::new)
                .collect(Collectors.toList());

        // 현재 페이지가 첫 페이지인지, 마지막 페이지인지에 대한 정보를 추출하여 DTO에 추가
        boolean isFirstPage = sleepoverApplies.isFirst();
        boolean isLastPage = sleepoverApplies.isLast();

        return SleepoverApplyDto.ResponseGetListSleepoverApply.from(getSleepoverApplyList, isFirstPage, isLastPage);
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
