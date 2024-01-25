package com.duktown.domain.replaceApply.dto;

import com.duktown.domain.replaceApply.entity.ReplaceApply;
import com.duktown.domain.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

public class ReplaceApplyDto {

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ReplaceApplyRequestDto{

        @NotNull(message = "교환할 사생 정보는 필수 입력입니다.")
        private Long replaceUserId;

        @NotNull(message = "날짜는 필수값입니다")
        @DateTimeFormat(pattern = "yyyy-MM-dd")
        private LocalDate originDate;

        private String reason;

        @NotNull(message = "날짜는 필수값입니다")
        @DateTimeFormat(pattern = "yyyy-MM-dd")
        private LocalDate replaceDate;

        public ReplaceApply toEntity(User user, ReplaceApplyDto.ReplaceApplyRequestDto requestDto, User replaceUser){
            return ReplaceApply.builder()
                    .user(user)
                    .replaceUser(replaceUser)
                    .originDate(requestDto.originDate)
                    .reason(requestDto.reason)
                    .replaceDate(requestDto.replaceDate)
                    .build();
        }
    }

}
