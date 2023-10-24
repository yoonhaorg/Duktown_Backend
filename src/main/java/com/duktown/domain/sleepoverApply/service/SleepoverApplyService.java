package com.duktown.domain.sleepoverApply.service;

import com.duktown.domain.sleepoverApply.dto.SleepoverApplyDto;
import com.duktown.domain.sleepoverApply.entity.SleepoverApply;
import com.duktown.domain.sleepoverApply.entity.SleepoverApplyRepository;
import com.duktown.domain.user.entity.User;
import com.duktown.domain.user.entity.UserRepository;
import com.duktown.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.duktown.global.exception.CustomErrorType.USER_NOT_FOUND;

@Service
@RequiredArgsConstructor
@Transactional
public class SleepoverApplyService {
    private final SleepoverApplyRepository sleepoverApplyRepository;
    private final UserRepository userRepository;

    public void createSleepoverApply(Long id, SleepoverApplyDto.RequestSleepoverApplyDto request){
        User user = userRepository.findById(id)
                .orElseThrow(() -> new CustomException(USER_NOT_FOUND));
         SleepoverApply sleepoverApply = request.toEntity(user);
         sleepoverApplyRepository.save(sleepoverApply);
    }

    @Transactional(readOnly = true)
    public SleepoverApplyDto.ResponseGetSleepoverApply getSleepoverApply(Long sleepoverApplyId){
       SleepoverApply getSleepoverApply = sleepoverApplyRepository.findById(sleepoverApplyId).get();
        return new SleepoverApplyDto.ResponseGetSleepoverApply(getSleepoverApply);
    }

    public void approveSleepoverApply(Long sleepoverApplyId){
       //sleepoverApplyRepository.update();
    }

}
