package com.duktown.domain.daily.service;

import com.duktown.domain.daily.dto.DailyDto;
import com.duktown.domain.daily.entity.Daily;
import com.duktown.domain.daily.entity.DailyRepository;
import com.duktown.domain.user.entity.User;
import com.duktown.domain.user.entity.UserRepository;
import com.duktown.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

import static com.duktown.global.exception.CustomErrorType.USER_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class DailyService {
    private final DailyRepository dailyRepository;
    private  final UserRepository userRepository;


    public void createDaily(Long userId, DailyDto.DailyRequest request){
        User user = userRepository.findById(userId)
                .orElseThrow(()->new CustomException(USER_NOT_FOUND));
        Daily daily = request.toEntity(user);
        dailyRepository.save(daily);
    }

    public DailyDto.GetDailyResponse getDaily(HttpServletRequest request, Long id){
        Daily daily = dailyRepository.findById(id).get();
        return new DailyDto.GetDailyResponse(daily);
    }

    public DailyDto.GetDailyListResponse getDailyList() {
        List<Daily> dailys = dailyRepository.findAll();
        List<DailyDto.DailyListResponse> dailyListResponses = dailys.stream()
                .map(DailyDto.DailyListResponse::new)
                .collect(Collectors.toList());

        return new DailyDto.GetDailyListResponse(dailyListResponses);
    }

    public void updateDaily(Long userId,Long id, DailyDto.DailyRequest request){
        Daily updatedaily = dailyRepository.findById(id).get(); // 오류처리 필요?
        User user = userRepository.findById(userId)
                .orElseThrow(()-> new CustomException(USER_NOT_FOUND));
        // 유저기 작성한 유저인지 확인 코드 필요?

        updatedaily.update(request.getTitle(),request.getContent());
        dailyRepository.save(updatedaily);


    }

    @Transactional
    public void deleteDaily(Long userId,Long id){
        Daily deletedaily = dailyRepository.findById(id).get();
        // 작성한 유저인지 체크
        User user = userRepository.findById(userId)
                .orElseThrow(()-> new CustomException(USER_NOT_FOUND));
        if(user.equals(deletedaily.getUser())){
            dailyRepository.delete(deletedaily);
        }

    }
}
