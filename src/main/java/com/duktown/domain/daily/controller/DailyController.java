package com.duktown.domain.daily.controller;

import com.duktown.domain.daily.dto.DailyDto;
import com.duktown.domain.daily.service.DailyService;
import com.duktown.global.security.service.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequestMapping("/dailys")
@RequiredArgsConstructor
public class DailyController {
    private final DailyService dailyService;

    @PostMapping
    public ResponseEntity<DailyDto> crateDaily(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @Valid @RequestBody DailyDto.DailyRequest request){
        dailyService.createDaily(customUserDetails.getId(),request);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<DailyDto.GetDailyListResponse> getDailyList(){
        return ResponseEntity.ok().body(dailyService.getDailyList());
    }

    @GetMapping("{dailyId}")
    public  ResponseEntity<DailyDto.GetDailyResponse> getDaily(
            HttpServletRequest request, @PathVariable("dailyId") Long id){
        return ResponseEntity.ok().body(dailyService.getDaily(request,id));
    }

    @PutMapping("{dailyId}")
    public  ResponseEntity<DailyDto> updateDaily(
            @Valid @RequestBody DailyDto.DailyRequest request,
            @PathVariable("dailyId") Long id,
            @AuthenticationPrincipal CustomUserDetails customUserDetails){
        dailyService.updateDaily(customUserDetails.getId(),id,request);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("{dailyId}")
    public ResponseEntity<Void> deleteDocument(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @PathVariable("dailyId") Long id){
        dailyService.deleteDaily(customUserDetails.getId(), id);
        return ResponseEntity.ok().build();
    }

}
