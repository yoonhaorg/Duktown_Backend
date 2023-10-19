package com.duktown.domain.like.entity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LikeRepository extends JpaRepository<Like, Long> {

    Like findByUserIdAndDeliveryId(Long userId, Long deliveryId);

    Like findByUserIdAndDailyId(Long userId, Long dailyId);

    Like findByUserIdAndMarketId(Long userId, Long marketId);

    Like findByUserIdAndCommentId(Long userId, Long commentId);

    @Query("select l from Like l " +
            "join fetch l.comment c " +
            "where l.user.id = :userId " +
            "and l.comment.id in :commentIds")
    List<Like> findAllByUserAndCommentIn(@Param("userId") Long userId, @Param("commentIds") List<Long> commentIds);

    void deleteByCommentId(Long commentId);
}
