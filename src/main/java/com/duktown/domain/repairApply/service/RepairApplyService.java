package com.duktown.domain.repairApply.service;


import com.duktown.domain.repairApply.dto.RepairApplyDto;
import com.duktown.domain.repairApply.entity.RepairApply;
import com.duktown.domain.repairApply.entity.RepairApplyRepository;
import com.duktown.domain.user.entity.User;
import com.duktown.domain.user.entity.UserRepository;
import com.duktown.global.exception.CustomException;
import com.duktown.global.type.HallName;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static com.duktown.global.exception.CustomErrorType.*;


@Service
@Transactional
@RequiredArgsConstructor
public class RepairApplyService {


    private final UserRepository userRepository;

    private final RepairApplyRepository repairApplyRepository;


    //등록
    public void saveApply(Long userId, RepairApplyDto.RepairApplyRequest apply) {
        User user = userRepository.findById(userId)
            .orElseThrow(()->new CustomException(USER_NOT_FOUND));

        HallName hallName = Arrays.stream(HallName.values())
                .filter(c -> c.getValue() == apply.getHallName())
                .findAny().orElseThrow(() -> new CustomException(INVALID_POST_CATEGORY_VALUE));

        RepairApply saveApply = apply.toEntity(user, hallName);
        repairApplyRepository.save(saveApply);
    }


    //수정
    public void updateApply(Long userId, Long repairApplyId, RepairApplyDto.RepairApplyRequest apply) {
        RepairApply updateApply = repairApplyRepository.findById(repairApplyId).orElseThrow(() -> new CustomException(POST_NOT_FOUND));
        User user = userRepository.findById(userId)
                .orElseThrow(()-> new CustomException(USER_NOT_FOUND));

        if (!updateApply.getUser().getId().equals(user.getId())) {
            throw new CustomException(HAVE_NO_PERMISSION);
        }

        updateApply.update(apply.getRoomNumber(), apply.getContent());
        repairApplyRepository.save(updateApply);
    }


    //삭제
    public void deleteApply(Long userId, Long repairApplyId) {
        RepairApply deleteApply = repairApplyRepository.findById(repairApplyId).orElseThrow(() -> new CustomException(POST_NOT_FOUND));
        User user = userRepository.findById(userId)
                .orElseThrow(()-> new CustomException(USER_NOT_FOUND));

        if (!deleteApply.getUser().getId().equals(user.getId())) {
            throw new CustomException(HAVE_NO_PERMISSION);
        }

        repairApplyRepository.delete(deleteApply);
    }

    //목록조회
    @Transactional(readOnly = true)
    public RepairApplyDto.RepairApplyListResponse getApplyList(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(()->new CustomException(USER_NOT_FOUND));

        List<RepairApply> applys = repairApplyRepository.findAllByCreatedAtThisYear();
        List<RepairApplyDto.RepairApplyResponseList> repairApplyResponseList = applys.stream()
                .map(RepairApplyDto.RepairApplyResponseList::fromEntity)
                .collect(Collectors.toList());

        return new RepairApplyDto.RepairApplyListResponse(repairApplyResponseList);
    }

    //상세조회
    @Transactional(readOnly = true)
    public RepairApplyDto.RepairApplyResponse getApply(Long userId, Long repairApplyId) {

        User user = userRepository.findById(userId).orElseThrow(() -> new CustomException(USER_NOT_FOUND));
        RepairApply apply = repairApplyRepository.findById(repairApplyId).orElseThrow(() -> new CustomException(POST_NOT_FOUND));

        return new RepairApplyDto.RepairApplyResponse(apply);
    }

    //check
    public void check(Long userId, Long repairApplyId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new CustomException(USER_NOT_FOUND));
        RepairApply checkApply = repairApplyRepository.findById(repairApplyId).orElseThrow(() -> new CustomException(POST_NOT_FOUND));

        checkApply.check();
        repairApplyRepository.save(checkApply);
    }

    //solve
    public void solve(Long userId, Long repairApplyId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new CustomException(USER_NOT_FOUND));
        RepairApply solveApply = repairApplyRepository.findById(repairApplyId).orElseThrow(() -> new CustomException(POST_NOT_FOUND));

        solveApply.solve();
        repairApplyRepository.save(solveApply);
    }


    // TODO: check -> 3일 동안 안 되면 재요청
//    @Scheduled(cron = "0 0 9 * * ?")
//    private void nextCheck(){
//
//    }
}
