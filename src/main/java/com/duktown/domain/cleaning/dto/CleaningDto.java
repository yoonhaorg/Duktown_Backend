package com.duktown.domain.cleaning.dto;

import com.duktown.domain.BaseTimeEntity;
import com.duktown.domain.cleaning.entity.Cleaning;
import com.duktown.domain.unitUser.entity.UnitUser;
import com.duktown.domain.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

public class CleaningDto extends BaseTimeEntity {

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CreateCleaningRequestDto{

        List<CreateCleaningUnit> cleaningUnit;


        @NoArgsConstructor
        @Getter
        public static class CreateCleaningUnit {

            @NotNull(message = "날짜는 필수값입니다")
            @DateTimeFormat(pattern = "yyyy-MM-dd")
            private LocalDate cleaningDate;
            private Long userId;
            }

    }


    @NoArgsConstructor
    @Getter
    public static class CleaningDateResponseDto{
        private Long cleaningId;
        private Long userId; //담당자
        private LocalDate cleaningDate;
        private Boolean cleaned;
        private Boolean checked;

        public CleaningDateResponseDto(Cleaning cleaning){
            cleaningId = cleaning.getId();
            userId= cleaning.getUser().getId();
            cleaningDate = cleaning.getDate();
            cleaned = cleaning.getCleaned();
            checked = cleaning.getChecked();
        }
    }
    @NoArgsConstructor
    @Getter
    public static class ListDto{
        List<CleaningDateResponseDto> data;
        public ListDto(List<CleaningDateResponseDto> data){
            this.data = data;
        }

    }


    @NoArgsConstructor
    @Getter
    public static class CleaningResponseDto{
        private LocalDate cleaningDate;
        private Boolean cleaned;
        private Boolean checked;

        public CleaningResponseDto(Cleaning cleaning){
            this.cleaningDate = cleaning.getDate();
            this.cleaned = cleaning.getCleaned();
            this.checked = cleaning.getChecked();
        }
    }


    @Getter
    @NoArgsConstructor
    public static class getCleaningListResponseDto{
        private List<CleaningResponseDto> content;

        public getCleaningListResponseDto(List<CleaningResponseDto> content){
            this.content =content;
        }
    }


    @Getter
    @AllArgsConstructor
    public static class getUserCleaningSchedule{
        private LocalDate cleaningDate;
    }

    @NoArgsConstructor
    @Getter
    public static class UserCleaningScheduleResponseDto{
        List<getUserCleaningSchedule> date;

        public UserCleaningScheduleResponseDto(List<getUserCleaningSchedule> date){
            this.date = date;
        }
    }


    @NoArgsConstructor
    @Getter
    public static class  unitCleaningResponse{
        private Long userId;
        private String UserName;

        public unitCleaningResponse(UnitUser unitUser){
            this.userId = unitUser.getUser().getId();
            this.UserName = unitUser.getUser().getName();
        }
    }

    @NoArgsConstructor
    @Getter
    public static class unitCleaningResponseDto{
        private List<unitCleaningResponse> unit;

        public unitCleaningResponseDto(List<unitCleaningResponse> unit ){
            this.unit = unit;
        }
    }


}
