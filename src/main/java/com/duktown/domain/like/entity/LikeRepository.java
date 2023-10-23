package com.duktown.domain.like.entity;

import com.duktown.domain.post.entity.Post;
import com.duktown.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LikeRepository extends JpaRepository<Like, Long> {

    Like findByUserIdAndDeliveryId(Long userId, Long deliveryId);

    Like findByUserIdAndPostId(Long userId, Long postId);

    Like findByUserIdAndCommentId(Long userId, Long commentId);

    @Query("select l from Like l " +
            "join fetch l.comment c " +
            "where l.user.id = :userId " +
            "and l.comment.id in :commentIds")
    List<Like> findAllByUserAndCommentIn(@Param("userId") Long userId, @Param("commentIds") List<Long> commentIds);

    @Query("select l from Like l " +
            "join fetch l.post p " +
            "where l.user.id = :userId " +
            "and l.post.id in :postIds")
    List<Like> findAllByUserAndPostIn(@Param("userId") Long userId, @Param("postIds") List<Long> postIds);

    List<Like> findAllByUserAndPost(User user, Post post);

    @Modifying
    @Query("delete from Like l where l.comment.id = :commentId")
    void deleteByCommentId(@Param("commentId") Long commentId);
}
