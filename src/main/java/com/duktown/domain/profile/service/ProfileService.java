package com.duktown.domain.profile.service;

import com.duktown.domain.profile.dto.ProfileDto;
import com.duktown.domain.user.entity.User;
import com.duktown.domain.user.entity.UserRepository;
import com.duktown.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.duktown.global.exception.CustomErrorType.USER_NOT_FOUND;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ProfileService {
    private final UserRepository userRepository;

    public ProfileDto.ProfileResponse getMyProfile(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new CustomException(USER_NOT_FOUND));

        // TODO: 유닛 정보 추가
        return ProfileDto.ProfileResponse.from(user);
    }
}
