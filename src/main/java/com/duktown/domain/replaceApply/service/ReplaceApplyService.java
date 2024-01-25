package com.duktown.domain.replaceApply.service;

import com.duktown.domain.replaceApply.dto.ReplaceApplyDto;
import com.duktown.domain.replaceApply.entity.ReplaceApply;
import com.duktown.domain.replaceApply.entity.ReplaceApplyRepository;
import com.duktown.domain.user.entity.User;
import com.duktown.domain.user.entity.UserRepository;
import com.duktown.global.exception.CustomErrorType;
import com.duktown.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReplaceApplyService {
    private final ReplaceApplyRepository replaceApplyRepository;
    private final UserRepository userRepository;

    @Transactional
    public void  createReplaceApply(Long userId,ReplaceApplyDto.ReplaceApplyRequestDto request){
        User user =  userRepository.findById(userId).orElseThrow(()-> new CustomException(CustomErrorType.USER_NOT_FOUND));
        User replaceUser = userRepository.findById(request.getReplaceUserId()).orElseThrow(()-> new CustomException(CustomErrorType.USER_NOT_FOUND));
        ReplaceApply replaceApply = request.toEntity(user, request, replaceUser);
        replaceApplyRepository.save(replaceApply);
    }
}
