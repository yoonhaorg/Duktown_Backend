package com.duktown.domain.cleaning.controller;

import com.duktown.domain.cleaning.dto.CleaningDto;
import com.duktown.domain.cleaning.service.CleaningService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/cleaning")
@RequiredArgsConstructor
public class CleaningController {
    private final CleaningService cleaningService;

    // 날짜별 청소 조회
     @GetMapping("/schedul")
     public ResponseEntity<CleaningDto.GetCleaningDate> getDateCleaning(@RequestParam("date") LocalDate date){
         CleaningDto.GetCleaningDate cleanDate = cleaningService.getCleanDate(date);
         return ResponseEntity.ok(cleanDate);
     }


    // 청소 완료
    @PatchMapping("/{cleaningId}")
    public ResponseEntity cleningOk(@PathVariable Long cleaningId){
        cleaningService.cleningOk(cleaningId);
        return ResponseEntity.ok().build();
    }

    // 청소 승인
    @PatchMapping("/manager/{cleaningId}")
    public ResponseEntity cleaningApply(@PathVariable Long cleaningId){
        cleaningService.cleaningApply(cleaningId);
        return ResponseEntity.ok().build();
    }

    // 벌점 부여


    // 청소 내역 조회
//    @GetMapping("cleaning/manager")
//
//    // 청소 배정
//    @PostMapping("cleaning/manager/schedul")

    // 유닛 조장이 청소 날짜 신청
    @PostMapping("/student/schedul")
    public ResponseEntity createCleaningDate(@RequestBody CleaningDto.CreateCleaningDto createCleaningDto) {
         cleaningService.createCleaningDate(createCleaningDto);
         return ResponseEntity.ok().build();
    }


}
