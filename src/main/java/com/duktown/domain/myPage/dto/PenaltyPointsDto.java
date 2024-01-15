package com.duktown.domain.myPage.dto;

import com.duktown.domain.penaltyPoints.entity.PenaltyPoints;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

public class PenaltyPointsDto {

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class PenaltyPointsResponseDto{
        private Long id;
        private Long user;
        private String reason;
        private int score;
        private LocalDate date;

        public PenaltyPointsResponseDto(PenaltyPoints penaltyPoints){
            this.id = penaltyPoints.getId();
            this.user = penaltyPoints.getUser().getId();
            this.reason = penaltyPoints.getReason();
            this.score = penaltyPoints.getScore();
            this.date = penaltyPoints.getDate();
        }
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class PenaltyPointsListResponseDto{
        private Long totalPenaltyPoint;
        private List<PenaltyPointsResponseDto> penaltyPointsList;

        public PenaltyPointsListResponseDto(List<PenaltyPointsResponseDto> penaltyPointsList, Long totalPenaltyPoint){
            this.penaltyPointsList = penaltyPointsList;
            this.totalPenaltyPoint = totalPenaltyPoint;

        }

    }
}
