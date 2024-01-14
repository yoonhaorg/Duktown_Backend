package com.duktown.domain.cleaning.dto;

import com.duktown.domain.BaseTimeEntity;
import com.duktown.domain.cleaning.entity.Cleaning;
import com.duktown.domain.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

public class CleaningDto extends BaseTimeEntity {

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CreateCleaningDto{

        List<CreateCleaningUnit> CleaningUnit;

        @AllArgsConstructor
        @NoArgsConstructor
        @Getter
        public static class CreateCleaningUnit {
            private LocalDate date;
            private String email;
            public  Cleaning toEntity(User user) {
                return Cleaning.builder()
                        .date(date)
                        .user(user)
                        .build();
            }

        }
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class GetCleaningDate{
        private LocalDate date;
        private User user;
        private Boolean cleaned;

        public GetCleaningDate(Cleaning cleaning){
            date = cleaning.getDate();
            user = cleaning.getUser();
            cleaned = cleaning.getCleaned();
        }
    }


}
