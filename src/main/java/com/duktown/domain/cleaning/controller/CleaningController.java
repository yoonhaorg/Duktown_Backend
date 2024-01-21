package com.duktown.domain.cleaning.controller;

import com.duktown.domain.cleaning.dto.CleaningDto;
import com.duktown.domain.cleaning.service.CleaningService;
import com.duktown.global.security.service.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping("/cleaning")
@RequiredArgsConstructor
public class CleaningController {
    private final CleaningService cleaningService;

    // 날짜별 청소 조회
     @GetMapping("/schedule")
     public ResponseEntity<CleaningDto.CleaningDateResponseDto> getDateCleaning(
             @AuthenticationPrincipal CustomUserDetails customUserDetails,
             @RequestBody CleaningDto.DateCleaningRequestDto date){
         CleaningDto.CleaningDateResponseDto cleanDate = cleaningService.getCleanDate(date,customUserDetails.getId());
         return ResponseEntity.ok(cleanDate);
     }


    // 청소 완료: 사생 청소 승인 요청
    @PatchMapping("/{cleaningId}")
    public ResponseEntity<Void> cleaningOk(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @PathVariable Long cleaningId){
        cleaningService.cleaningOk(cleaningId);
        return ResponseEntity.ok().build();
    }

    // 청소 승인 : 사생회 청소 승인
    @PatchMapping("/manager/{cleaningId}")
    public ResponseEntity<Void> cleaningApply(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @PathVariable Long cleaningId){
        cleaningService.checkOk(cleaningId);
        return ResponseEntity.ok().build();
    }

    // 나의 청소 완료 목록 조회
    @GetMapping("")
    public ResponseEntity<CleaningDto.getCleaningListResponseDto>  getMyCleaningList(
            @AuthenticationPrincipal CustomUserDetails customUserDetails){
        return ResponseEntity.ok().body(
                cleaningService.getMyCleanedList(customUserDetails.getId()));
    }

    // 유닛 조장이 청소 날짜 배정
    @PostMapping("/student/schedule")
//    public ResponseEntity<?> createCleaningDate(@RequestBody CleaningDto.CreateCleaningRequestDto createCleaningDto) {
    public ResponseEntity<?> createCleaningDate(@RequestBody CleaningDto.CreateCleaningRequestDto createCleaningDto) {
        cleaningService.createCleaningDate(createCleaningDto);
         return ResponseEntity.status(CREATED).build();
    }

    // 청소 유닛 조회
    @GetMapping("/unit")
    public ResponseEntity<?> getCleaningUnit(@AuthenticationPrincipal CustomUserDetails customUserDetails){
         return ResponseEntity.ok().body(cleaningService.myUnit(customUserDetails.getId()));
    }

    //사생별 청소 일정 조회
    @GetMapping("/{userId}/schedule")
    public ResponseEntity<CleaningDto.UserCleaningScheduleResponseDto> getStudentsSchedule(
            @PathVariable Long userId,
            @AuthenticationPrincipal CustomUserDetails customUserDetails){
        return ResponseEntity.ok().body(
                cleaningService.StudentSchedule(userId));
    }
}
