package com.duktown.domain.sleepoverApply.dto;

import com.duktown.domain.BaseTimeEntity;
import com.duktown.domain.sleepoverApply.entity.Address;
import com.duktown.domain.sleepoverApply.entity.SleepoverApply;
import com.duktown.domain.user.entity.User;
import com.duktown.global.type.ApprovalType;
import lombok.*;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class SleepoverApplyDto {

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class RequestSleepoverApplyDto{

        @NotNull
        @FutureOrPresent(message = "외박 신청 날짜는 현재 시간 이후거나 동일해야 합니다.")
        private LocalDate startDate; //외박 시작 날짜
        @NotNull
        @Future(message = "귀가 날짜는 미래여야 합니다.")
        private LocalDate endDate; //돌아오는 날짜

        @Min(value = 1)
        private Integer period; //외박 일 수

        private String zipcode; // 우편번호
        @NotBlank(message = "머무는 곳의 주소는 필수 값입니다")
        private String streetAddress;// 지번 주소
        private String detailAddress;// 상세 주소
        @NotBlank(message = "사유는 필수 값입니다.")
        private String reason; //사유

        public SleepoverApply toEntity(User user){
            return SleepoverApply.builder()
                    .user(user)
                    .startDate(startDate)
                    .endDate(endDate)
                    .period(period)
                    .address(new Address(zipcode,streetAddress,detailAddress))
                    .reason(reason)
                    .approved(ApprovalType.Waiting)
                    .build();
        }
    }

    @Getter
    @AllArgsConstructor
    public static class ResponseGetSleepoverApply{

        private Long userId;
        private Long sleepoverApplyId;
        private LocalDate startDate;
        private LocalDate endDate;
        private Integer period; //외박 일 수
        private String address; // 머무르는 주소
        private String reason; //사유

        public ResponseGetSleepoverApply(SleepoverApply sleepoverApply){
            sleepoverApplyId  = sleepoverApply.getId();
            startDate = sleepoverApply.getStartDate();
            endDate= sleepoverApply.getEndDate();
            period= sleepoverApply.getPeriod();
            address=  sleepoverApply.getAddress().getStreetAddress() + sleepoverApply.getAddress().getStreetAddress();
            reason =sleepoverApply.getReason();
            userId = sleepoverApply.getUser().getId();

        }
    }

    @Getter
    @Builder
    @AllArgsConstructor
    public static class ResponseGetSleepoverApplyFromManager{
        private Integer sleepoverApplyCount;
        private boolean isFirstPage;
        private boolean isLastPage;

        private List<ResponseGetSleepoverApply> listSleepoverApply;

        public static ResponseGetSleepoverApplyFromManager from(List<ResponseGetSleepoverApply> getSleepoverApplyList, boolean isFirstPage, boolean isLastPage){
            return ResponseGetSleepoverApplyFromManager.builder()
                    .sleepoverApplyCount(getSleepoverApplyList.size())
                    .listSleepoverApply(getSleepoverApplyList)
                    .build();
        }
    }




    @Getter
    @Builder
    @AllArgsConstructor
    public static class ResponseGetListSleepoverApply{

        private Long sleepoverApplyId;
        private LocalDate startDate;
        private LocalDate endDate;
        private LocalDateTime createdAt;
        private String address; // 머무르는 주소
        private Integer period; //외박 일 수

        public ResponseGetListSleepoverApply(SleepoverApply sleepoverApply){
            sleepoverApplyId  = sleepoverApply.getId();
            startDate = sleepoverApply.getStartDate();
            endDate= sleepoverApply.getEndDate();
            period= sleepoverApply.getPeriod();
            address=  sleepoverApply.getAddress().getStreetAddress() + sleepoverApply.getAddress().getStreetAddress();
            createdAt =sleepoverApply.getCreatedAt();
        }

    }

    @Getter
    @Builder
    @AllArgsConstructor
    public static class ResponseGetSleepoverApplyFromStudent{
        private Integer availablePeriod;

        private List<ResponseGetListSleepoverApply> content;

        public static ResponseGetSleepoverApplyFromStudent from(List<ResponseGetListSleepoverApply> getSleepoverApplyList,Integer availablePeriod){
            return ResponseGetSleepoverApplyFromStudent.builder()
                    .content(getSleepoverApplyList)
                    .availablePeriod(availablePeriod)
                    .build();
        }
    }

    @Getter
    @AllArgsConstructor
    public static class ResponseGetAvailablePeriod{
        private Integer availablePeriod;

    }


}
