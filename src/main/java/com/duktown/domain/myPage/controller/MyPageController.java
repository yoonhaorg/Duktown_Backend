package com.duktown.domain.myPage.controller;

import com.duktown.domain.myPage.service.MyPageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/my")
public class MyPageController {
    private final MyPageService myPageService;

    // 나의 벌점 내역 조회
    @GetMapping("/PenaltyPoints")
    public ResponseEntity<?> getMyPenaltyPoints(){
        return null;
    }
    // 관리자, 사생회: 사생에게 벌점 부과도 여기?

    // 나의 유닛 조회
    @GetMapping("/Units")
    public ResponseEntity<?> getMyUnits(){
        return null;
    }

    //TODO: 기획에 따라서 관리자-사생회-유닛장 등 신청 및 승인 로직인지? 확인하기
    // 유닛장 등록
    // 사생회 유닛장 승인
    // 사생회 등록
    // 관리자 사생회 승인



}
