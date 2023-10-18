package com.duktown.domain.like.dto;

import com.duktown.domain.comment.entity.Comment;
import com.duktown.domain.daily.entity.Daily;
import com.duktown.domain.delivery.entity.Delivery;
import com.duktown.domain.like.entity.Like;
import com.duktown.domain.market.entity.Market;
import com.duktown.domain.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class LikeDto {

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class LikeRequest {
        private Long deliveryId;
        private Long dailyId;
        private Long marketId;
        private Long commentId;

        public static Like toEntity(User user, Delivery delivery, Daily daily, Market market, Comment comment) {
            return Like.builder()
                    .delivery(delivery)
                    .daily(daily)
                    .market(market)
                    .comment(comment)
                    .build();
        }
    }
}
