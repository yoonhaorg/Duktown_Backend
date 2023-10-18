package com.duktown.domain.like.entity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface LikeRepository extends JpaRepository<Like, Long> {

    @Query(value = "select l from Like as l where l.user.id = :user_id and l.delivery.id = :delivery_id")
    Like findByUserIdAndDeliveryId(@Param("user_id") Long userId, @Param("delivery_id") Long deliveryId);

    @Query(value = "select l from Like as l where l.user.id = :user_id and l.daily.id = :daily_id")
    Like findByUserIdAndDailyId(@Param("user_id") Long userId, @Param("daily_id") Long dailyId);

    @Query(value = "select l from Like as l where l.user.id = :user_id and l.daily.id = :market_id")
    Like findByUserIdAndMarketId(@Param("user_id") Long userId, @Param("market_id") Long marketId);

    @Query(value = "select l from Like as l where l.user.id = :user_id and l.comment.id = :comment_id")
    Like findByUserIdAndCommentId(@Param("user_id") Long userId, @Param("comment_id") Long commentId);
}
