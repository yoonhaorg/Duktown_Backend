package com.duktown.domain.repairApply.service;


import com.duktown.domain.repairApply.dto.RepairApplyDto;
import com.duktown.domain.repairApply.entity.RepairApply;
import com.duktown.domain.repairApply.entity.RepairApplyRepository;
import com.duktown.domain.user.entity.User;
import com.duktown.domain.user.entity.UserRepository;
import com.duktown.global.exception.CustomException;
import com.duktown.global.type.HallName;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.duktown.global.exception.CustomErrorType.*;

@Service
@Transactional
@RequiredArgsConstructor
//public class RepairApplyServiceImpl implements RepairApplyService{
public class RepairApplyServiceImpl {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RepairApplyRepository repairApplyRepository;

//    @Override
//    public RepairApply saveApply(RepairApply apply) {
//
//        apply.setChecked(false);
//        apply.setSolved(false);
//
//        return repairApplyRepository.save(apply);
//    }

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

//    @Override
//    public int updateApply(RepairApply apply) {
//        Optional<RepairApply> findApply = repairApplyRepository.findById(apply.getId());
//
//        int result = 0;
//        if (findApply.isPresent()) {
//            RepairApply dbApply = findApply.get();
//            dbApply.setUnit(apply.getUnit());
//            dbApply.setContent(apply.getContent());
//            repairApplyRepository.save(dbApply);
//            result = 1;
//        }
//        return result;
//    }

    //수정
    public void updateApply(Long userId, Long repairApplyId, RepairApplyDto.RepairApplyRequest apply) {
        RepairApply updateApply = repairApplyRepository.findById(repairApplyId).orElseThrow(() -> new CustomException(POST_NOT_FOUND));
        User user = userRepository.findById(userId)
                .orElseThrow(()-> new CustomException(USER_NOT_FOUND));

        if (!updateApply.getUser().getId().equals(user.getId())) {
            throw new CustomException(HAVE_NO_PERMISSION);
        }

        updateApply.update(apply.getUnit(), apply.getContent());
        repairApplyRepository.save(updateApply);
    }

//    @Override
//    public int deleteApply(Long id) {
//        Optional<RepairApply> findApply = repairApplyRepository.findById(id);
//
//        int result = 0;
//        if (findApply.isPresent()) {
//            repairApplyRepository.delete(findApply.get());
//            result = 1;
//        }
//        return result;
//    }

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

//    @Override
//    public List<RepairApply> findAll() {
//        return repairApplyRepository.findAll();
//    }

    //목록조회
    @Transactional(readOnly = true)
//    public RepairApplyDto.RepairApplyListResponse getApplyList(Long userId, Integer hallName) {
    public RepairApplyDto.RepairApplyListResponse getApplyList(Long userId, Integer hallName) {
        User user = userRepository.findById(userId)
                .orElseThrow(()->new CustomException(USER_NOT_FOUND));

        HallName findHallName = Arrays.stream(HallName.values())
                .filter(c -> c.getValue() == hallName)
                .findAny().orElseThrow(() -> new CustomException(INVALID_POST_CATEGORY_VALUE));

//
//        List<RepairApply> applys = repairApplyRepository.findAllByHallName(findHallName);

        List<RepairApply> applys = repairApplyRepository.findAll();

        return new RepairApplyDto.RepairApplyListResponse(applys);
    }

//    @Override
//    public List<RepairApply> findByUser(Long id) {
//        Optional<User> findUser = userRepository.findById(id);
//
//        if (findUser.isPresent()) {
//            return repairApplyRepository.findByUser(findUser.get());
//        }
//        return null;
//    }

    //상세조회
    @Transactional(readOnly = true)
    public RepairApplyDto.RepairApplyResponse getApply(Long userId, Long repairApplyId) {

        User user = userRepository.findById(userId).orElseThrow(() -> new CustomException(USER_NOT_FOUND));
        RepairApply apply = repairApplyRepository.findById(repairApplyId).orElseThrow(() -> new CustomException(POST_NOT_FOUND));

        return new RepairApplyDto.RepairApplyResponse(apply);
    }

//    @Override
//    public RepairApply findById(Long id) {
//        Optional<RepairApply> findApply = repairApplyRepository.findById(id);
//
//        if (findApply.isPresent()) {
//            return findApply.get();
//        }
//        return null;
//    }

//    @Override
//    public int checkApply(RepairApply apply) {
//        Optional<RepairApply> findApply = repairApplyRepository.findById(apply.getId());
//
//        int result = 0;
//        if (findApply.isPresent()) {
//            RepairApply dbApply = findApply.get();
//            dbApply.setChecked(true);
//            repairApplyRepository.save(dbApply);
//            result = 1;
//        }
//        return result;
//    }
//
//    @Override
//    public int solveApply(RepairApply apply) {
//        Optional<RepairApply> findApply = repairApplyRepository.findById(apply.getId());
//
//        int result = 0;
//        if (findApply.isPresent()) {
//            RepairApply dbApply = findApply.get();
//            dbApply.setSolved(true);
//            repairApplyRepository.save(dbApply);
//            result = 1;
//        }
//        return result;
//    }

    //check -> 3일 동안 안 되면 재요청
    public void check(Long userId, Long repairApplyId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new CustomException(USER_NOT_FOUND));
        RepairApply checkApply = repairApplyRepository.findById(repairApplyId).orElseThrow(() -> new CustomException(POST_NOT_FOUND));

        checkApply.check();
        repairApplyRepository.save(checkApply);
    }

    //solve ->
    public void solve(Long userId, Long repairApplyId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new CustomException(USER_NOT_FOUND));
        RepairApply solveApply = repairApplyRepository.findById(repairApplyId).orElseThrow(() -> new CustomException(POST_NOT_FOUND));

        solveApply.solve();
        repairApplyRepository.save(solveApply);
    }


    @Scheduled(cron = "0 0 9 * * ?")
    private void nextCheck(){

    }
}
