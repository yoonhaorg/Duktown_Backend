package com.duktown.domain.comment.entity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    @Query(value = "select c from Comment as c where c.parentComment.id is null and c.delivery.id = :delivery_id")
    List<Comment> findParentCommentsByDeliveryId(@Param("delivery_id") Long deliveryId);

    @Query(value = "select c from Comment as c where c.parentComment.id is null and c.daily.id = :daily_id")
    List<Comment> findParentCommentsByDailyId(@Param("daily_id") Long dailyId);

    @Query(value = "select c from Comment as c where c.parentComment.id is null and c.market.id = :market_id")
    List<Comment> findParentCommentsByMarketId(@Param("market_id") Long marketId);

    List<Comment> findAllByDeliveryId(Long deliveryId);
    List<Comment> findAllByDailyId(Long dailyId);
    List<Comment> findAllByMarketId(Long marketId);

    Boolean existsByParentCommentId(Long parentCommentId);

    @Query(value = "select count(*) from comment where delivery_id = :delivery_id and comment.deleted = false", nativeQuery = true)
    Long countByDeliveryId(@Param("delivery_id") Long deliveryId);

    @Query(value = "select count(*) from comment where daily_id = :daily_id and comment.deleted = false", nativeQuery = true)
    Long countByDailyId(@Param("daily_id") Long dailyId);

    @Query(value = "select count(*) from comment where market_id = :market_id and comment.deleted = false", nativeQuery = true)
    Long countByMarketId(@Param("market_id") Long marketId);
}
