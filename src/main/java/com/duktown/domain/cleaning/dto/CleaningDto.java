package com.duktown.domain.cleaning.dto;

import com.duktown.domain.BaseTimeEntity;
import com.duktown.domain.cleaning.entity.Cleaning;
import com.duktown.domain.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class CleaningDto extends BaseTimeEntity {

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class DateCleaningRequestDto {

        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
        private LocalDate cleaningDate;
    }


    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CreateCleaningRequestDto{

//        List<CreateCleaningUnit> CleaningUnit = new ArrayList<>();
//
//        @AllArgsConstructor
//        @NoArgsConstructor
//        @Getter
//        public static class CreateCleaningUnit {
            private LocalDate cleaningDate;
            private String email;
            public  Cleaning toEntity(User user) {
                return Cleaning.builder()
                        .date(cleaningDate)
                        .user(user)
                        .build();
            }
        //}
    }


    @NoArgsConstructor
    @Getter
    public static class CleaningDateResponseSto{
        private LocalDate cleaningDate;
        //private User userid;
        private Boolean cleaned;

        public CleaningDateResponseSto(Cleaning cleaning){
            cleaningDate = cleaning.getDate();
            cleaned = cleaning.getCleaned();
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
    public static class getCleaningListResponseDto{
        private List<CleaningResponseDto> content;

        public getCleaningListResponseDto(List<CleaningResponseDto> content){
            this.content =content;
        }
    }



}
