package com.duktown.domain.daily.dto;

import com.duktown.domain.daily.entity.Daily;
import com.duktown.domain.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.util.List;

public class DailyDto {
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class DailyRequest{
        //private User user;
        //private Long userId;
//        private String accessToken;

        @NotBlank(message = "제목은 필수 입력값입니다.")
        private String title;

        @NotBlank(message = "내용은 필수 입력값입니다")
        private String content;

        // DTO -> Entity
        public Daily toEntity(User user){
            return Daily.builder()
                    .user(user)
                    .title(title)
                    .content(content)
                    .build();
        }

    }

    @Getter
    @AllArgsConstructor
    public static class GetDailyResponse {
        private Long id;
        private Long userId;
        private String title;
        private String content;
        private String datetime;

        // from
        public GetDailyResponse(Daily daily){
            this.userId =daily.getUser().getId();
            this.id = daily.getId();
            this.title =daily.getTitle();
            this.content =daily.getContent();
            this.datetime = daily.getCreatedAt().toString();

        }
    }


    // 목록을..

    @Getter
    @AllArgsConstructor
    public  static class DailyListResponse{
        private Long id;
        private String title;
        private String content;
        private String datetime;

        public  DailyListResponse(Daily daily){
            this.id = daily.getId();
            this.title =daily.getTitle();
            this.content =daily.getContent();
            this.datetime = daily.getCreatedAt().toString();
        }
    }


    @Getter
    public  static class  GetDailyListResponse{
        private List<DailyListResponse> content;
        public  GetDailyListResponse(List<DailyListResponse> content){
            this.content = content;
        }
    }
}
