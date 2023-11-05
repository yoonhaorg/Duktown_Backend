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

    @Query(value = "select c from Comment as c where c.parentComment.id is null and c.post.id = :post_id")
    List<Comment> findParentCommentsByPostId(@Param("post_id") Long postId);

    List<Comment> findAllByDeliveryId(Long deliveryId);

    List<Comment> findAllByPostId(Long postId);

    Boolean existsByParentCommentId(Long parentCommentId);

    @Query(value = "select count(*) from comment where delivery_id = :delivery_id and comment.deleted = false", nativeQuery = true)
    Long countByDeliveryId(@Param("delivery_id") Long deliveryId);

    @Query(value = "select count(*) from comment where post_id = :post_id and comment.deleted = false", nativeQuery = true)
    Long countByPostId(@Param("post_id") Long postId);
}
