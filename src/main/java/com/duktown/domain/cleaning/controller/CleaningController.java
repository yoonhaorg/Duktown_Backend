package com.duktown.domain.cleaning.controller;

import com.duktown.domain.cleaning.dto.CleaningDto;
import com.duktown.domain.cleaning.service.CleaningService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/cleaning")
@RequiredArgsConstructor
public class CleaningController {
    private final CleaningService cleaningService;

    // 날짜별 청소 조회
     @GetMapping("/schedule")
     public ResponseEntity<CleaningDto.GetCleaningDate> getDateCleaning(@RequestParam("date") LocalDate date){
         CleaningDto.GetCleaningDate cleanDate = cleaningService.getCleanDate(date);
         return ResponseEntity.ok(cleanDate);
     }


    // 청소 완료: 사생 청소 승인 요청
    @PatchMapping("/{cleaningId}")
    public ResponseEntity cleningOk(@PathVariable Long cleaningId){
        cleaningService.CheckCleaning(cleaningId);
        return ResponseEntity.ok().build();
    }

    // 청소 승인 : 사생회 청소 승인
    @PatchMapping("/manager/{cleaningId}")
    public ResponseEntity cleaningApply(@PathVariable Long cleaningId){
        cleaningService.cleaningApply(cleaningId);
        return ResponseEntity.ok().build();
    }

    // 나의 청소 완료 목록 조회


    // 유닛 조장이 청소 날짜 신청
    @PostMapping("/student/schedule")
    public ResponseEntity createCleaningDate(@RequestBody CleaningDto.CreateCleaningDto createCleaningDto) {
         cleaningService.createCleaningDate(createCleaningDto);
         return ResponseEntity.ok(HttpStatus.CREATED);
    }


}
