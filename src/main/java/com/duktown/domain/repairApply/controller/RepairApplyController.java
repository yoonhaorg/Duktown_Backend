package com.duktown.domain.repairApply.controller;

import com.duktown.domain.repairApply.dto.RepairApplyDto;
import com.duktown.domain.repairApply.service.RepairApplyServiceImpl;
import com.duktown.global.security.service.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/repairApply")
@RequiredArgsConstructor
public class RepairApplyController {


    private final RepairApplyServiceImpl repairApplyService;

//    @PostMapping
//    public ResponseEntity<RepairApply> save(
//            @AuthenticationPrincipal CustomUserDetails customUserDetails,
//            @Valid @RequestBody RepairApply apply) {
//
//        try {
//            //apply.setUser(customUserDetails.getUser());
//            RepairApply saveApply = repairApplyService.saveApply(apply);
//            if (saveApply != null) {
//                return new ResponseEntity<>(saveApply, HttpStatus.CREATED);
//            }
//        } catch (Exception e) { }
//        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
//    }
//
//    @PutMapping
//    public ResponseEntity<RepairApply> update(
//            @AuthenticationPrincipal CustomUserDetails customUserDetails,
//            @Valid @RequestBody RepairApply apply) {
//        try {
//            int result = repairApplyService.updateApply(apply);
//
//            if (result == 1) {
//                RepairApply updateApply = repairApplyService.findById(apply.getId());
//                return new ResponseEntity<>(updateApply, HttpStatus.OK);
//            }
//        } catch (Exception e) { }
//        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
//
//    }
//
//    @DeleteMapping
//    public ResponseEntity<Long> delete(
//            @AuthenticationPrincipal CustomUserDetails customUserDetails,
//            @Valid @PathVariable long id) {
//        try {
//            int result = repairApplyService.deleteApply(id);
//
//            if (result == 1) {
//                return new ResponseEntity<>(HttpStatus.OK);
//            }
//        } catch (Exception e) { }
//        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
//
//    }
//
//    @GetMapping("/{repairApplyId}")
//    public ResponseEntity<RepairApply> findById(
//            @AuthenticationPrincipal CustomUserDetails customUserDetails,
//            @Valid @PathVariable("repairApplyId") long id) {
//        try {
//            RepairApply findApply = repairApplyService.findById(id);
//
//            if (findApply != null) {
//                return new ResponseEntity<>(findApply, HttpStatus.OK);
//            }
//        } catch (Exception e) { }
//        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
//
//    }
//
//    @GetMapping
//    public ResponseEntity<List<RepairApply>> findAll() {
//        try {
//            List<RepairApply> applyList = repairApplyService.findAll();
//
//            if (applyList != null) {
//                return new ResponseEntity<>(applyList, HttpStatus.OK);
//            }
//        } catch (Exception e) { }
//        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
//
//    }
//
//    @GetMapping("/my/{userId}")
//    public ResponseEntity<List<RepairApply>> findByUser(
//            @AuthenticationPrincipal CustomUserDetails customUserDetails,
//            @Valid @PathVariable("userId") Long id) {
//        try {
//            List<RepairApply> applyList = repairApplyService.findByUser(id);
//
//            if (applyList != null) {
//                return new ResponseEntity<>(applyList, HttpStatus.OK);
//            }
//        } catch (Exception e) { }
//        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
//
//    }
//
//    @PutMapping("/check/{repairApplyId}")
//    public ResponseEntity<RepairApply> check(
//            @AuthenticationPrincipal CustomUserDetails customUserDetails,
//            @Valid @RequestBody RepairApply apply) {
//        try {
//            int result = repairApplyService.checkApply(apply);
//
//            if (result == 1) {
//                RepairApply updateApply = repairApplyService.findById(apply.getId());
//                return new ResponseEntity<>(updateApply, HttpStatus.OK);
//            }
//        } catch (Exception e) { }
//        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
//
//    }
//
//    @PutMapping("/solve/{repairApplyId}")
//    public ResponseEntity<RepairApply> solve(
//            @AuthenticationPrincipal CustomUserDetails customUserDetails,
//            @Valid @RequestBody RepairApply apply) {
//        try {
//            int result = repairApplyService.solveApply(apply);
//
//            if (result == 1) {
//                RepairApply updateApply = repairApplyService.findById(apply.getId());
//                return new ResponseEntity<>(updateApply, HttpStatus.OK);
//            }
//        } catch (Exception e) { }
//        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
//
//    }

    @PostMapping
    public ResponseEntity<Void> create(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @Valid @RequestBody RepairApplyDto.RepairApplyRequest request){
        repairApplyService.saveApply(customUserDetails.getId(), request);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<RepairApplyDto.RepairApplyListResponse> getApplyList(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @RequestParam(value = "hallName") Integer hallName
    ){
        return ResponseEntity.ok().body(
                repairApplyService.getApplyList(customUserDetails.getId(), hallName));
    }

    @GetMapping("/{repairApplyId}")
    public ResponseEntity<RepairApplyDto.RepairApplyResponse> getApply(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @PathVariable("repairApplyId") Long repairApplyId){
        return ResponseEntity.ok().body(repairApplyService.getApply(customUserDetails.getId(), repairApplyId));
    }

    @PutMapping("/{repairApplyId}")
    public ResponseEntity<RepairApplyDto> update(
            @Valid @RequestBody RepairApplyDto.RepairApplyRequest request,
            @PathVariable("repairApplyId") Long repairApplyId,
            @AuthenticationPrincipal CustomUserDetails customUserDetails){
        repairApplyService.updateApply(customUserDetails.getId(), repairApplyId, request);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/check/{repairApplyId}")
    public ResponseEntity<RepairApplyDto> check(
//            @Valid @RequestBody RepairApplyDto.RepairApplyRequest request,
            @PathVariable("repairApplyId") Long repairApplyId,
            @AuthenticationPrincipal CustomUserDetails customUserDetails){
        repairApplyService.check(customUserDetails.getId(), repairApplyId);
        return ResponseEntity.ok().build();
    }
    @PutMapping("/solve/{repairApplyId}")
    public ResponseEntity<RepairApplyDto> solve(
//            @Valid @RequestBody RepairApplyDto.RepairApplyRequest request,
            @PathVariable("repairApplyId") Long repairApplyId,
            @AuthenticationPrincipal CustomUserDetails customUserDetails){
        repairApplyService.solve(customUserDetails.getId(), repairApplyId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{repairApplyId}")
    public ResponseEntity<Void> delete(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @PathVariable("repairApplyId") Long repairApplyId){
        repairApplyService.deleteApply(customUserDetails.getId(), repairApplyId);
        return ResponseEntity.ok().build();
    }
    
    
}
