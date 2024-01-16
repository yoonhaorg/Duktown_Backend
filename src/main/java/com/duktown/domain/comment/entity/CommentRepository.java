package com.duktown.domain.comment.entity;

import com.duktown.global.type.Category;
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

    @Query("select c from Comment c join fetch c.delivery d where c.user.id = :user_id" +
            " and d.deleted = false" +
            " order by c.createdAt desc")
    List<Comment> findAllByUserAndDeliverySortByCreatedAt(@Param("user_id") Long userId);

    @Query("select c from Comment c join fetch c.delivery d where c.user.id = :user_id " +
            "and d.deleted = false " +
            "order by d.orderTime")
    List<Comment> findAllByUserAndDeliverySortByOrderTime(@Param("user_id") Long userId);

    @Query("select c from Comment c join fetch c.post p " +
            "where c.user.id = :user_id and p.category = :category order by p.createdAt desc")
    List<Comment> findAllByUserAndPostAndCategory(@Param("user_id") Long userId, @Param(value = "category") Category category);

    @Query("select count(distinct c.user) from Comment c where c.delivery.id = :delivery_id")
    Long countUniqueUsersByDeliveryId(@Param("delivery_id") Long deliveryId);

    @Query("select count(distinct c.user) from Comment c where c.post.id = :post_id")
    Long countUniqueUsersByPostId(@Param("post_id") Long postId);

    Boolean existsByUserIdAndPostId(Long userId, Long postId);

    Boolean existsByUserIdAndDeliveryId(Long userId, Long deliveryId);

    Comment findFirstByUserIdAndPostId(Long userId, Long postId);

    Comment findFirstByUserIdAndDeliveryId(Long userId, Long deliveryId);
}
