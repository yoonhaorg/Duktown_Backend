package com.duktown.domain.comment.entity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findAllByDeliveryId(Long deliveryId);
    List<Comment> findAllByDailyId(Long dailyId);
    List<Comment> findAllByMarketId(Long marketId);
    Boolean existsByParentCommentId(Long parentCommentId);
}
