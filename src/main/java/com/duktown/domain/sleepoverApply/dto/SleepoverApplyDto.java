package com.duktown.domain.sleepoverApply.dto;

import com.duktown.domain.sleepoverApply.entity.SleepoverApply;
import com.duktown.domain.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

public class SleepoverApplyDto {

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class RequestSleepoverApplyDto{

        @NotNull
        private LocalDate startDate; //외박 시작 날짜
        @NotNull
        private LocalDate endDate; //돌아오는 날짜

        @Min(value = 1)
        private Integer period; //외박 일 수
        @NotBlank(message = "머무는 곳의 주소는 필수 값입니다")
        private String address; // 머무르는 주소
        @NotBlank(message = "사유는 필수 값입니다.")
        private String reason; //사유

        public SleepoverApply toEntity(User user){
            return SleepoverApply.builder()
                    .user(user)
                    .startDate(startDate)
                    .endDate(endDate)
                    .period(period)
                    .address(address)
                    .reason(reason)
                    .build();
        }
    }

    @Getter
    @AllArgsConstructor
    public static class ResponseGetSleepoverApply{
        private Long userId;
        private LocalDate startDate;
        private LocalDate endDate;
        private Integer period; //외박 일 수
        private String address; // 머무르는 주소
        private String reason; //사유

       public ResponseGetSleepoverApply(SleepoverApply sleepoverApply){
           startDate = sleepoverApply.getStartDate();
           endDate= sleepoverApply.getEndDate();
           period= sleepoverApply.getPeriod();
           address=  sleepoverApply.getAddress();
           userId= sleepoverApply.getUser().getId();
           reason =sleepoverApply.getReason();

        }
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class RequestApproveSleepoverApply{
        private Boolean approved;
    }

}
