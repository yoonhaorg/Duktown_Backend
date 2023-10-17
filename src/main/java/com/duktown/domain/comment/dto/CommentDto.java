package com.duktown.domain.comment.dto;

import com.duktown.domain.comment.entity.Comment;
import com.duktown.domain.daily.entity.Daily;
import com.duktown.domain.delivery.entity.Delivery;
import com.duktown.domain.market.entity.Market;
import com.duktown.domain.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

public class CommentDto {

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class CreateRequest{
        private Long deliveryId;
        private Long dailyId;
        private Long marketId;
        private Long parentCommentId;
        @NotBlank(message = "댓글 내용은 필수 값입니다.")
        private String content;

        public Comment toEntity(User user, Delivery delivery, Daily daily, Market market, Comment parentComment) {
            return Comment.builder()
                    .user(user)
                    .delivery(delivery)
                    .daily(daily)
                    .market(market)
                    .parentComment(parentComment)
                    .content(content)
                    .deleted(false)
                    .build();
        }
    }
}
