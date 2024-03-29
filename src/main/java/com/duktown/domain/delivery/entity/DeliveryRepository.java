package com.duktown.domain.delivery.entity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DeliveryRepository extends JpaRepository<Delivery, Long> {

    @Query(value = "select d from Delivery d where d.id = :deliveryId and d.deleted = false and d.user.deleted = false")
    Optional<Delivery> findById(@Param("deliveryId") Long deliveryId);

    @Query(value = "select d from Delivery d where d.chatRoom.id = :chatRoomId and d.user.deleted = false")
    Optional<Delivery> findByChatRoomId(@Param("chatRoomId") Long chatRoomId);

    @Query(value = "select d from Delivery d where d.deleted = false and d.user.deleted = false order by d.createdAt desc")
    List<Delivery> findAllSortByCreatedAt();

    @Query(value = "select d from Delivery d where d.deleted = false and d.user.deleted = false order by d.orderTime")
    List<Delivery> findAllSortByOrderTime();

    @Query(value = "select d from Delivery d where d.user.id = :user_id and d.deleted = false and d.user.deleted = false order by d.createdAt desc")
    List<Delivery> findAllByUserIdSortByCreatedAt(@Param("user_id") Long userId);

    @Query(value = "select d from Delivery d where d.user.id = :user_id and d.deleted = false and d.user.deleted = false order by d.orderTime")
    List<Delivery> findAllByUserIdSortByOrderTime(@Param("user_id") Long userId);

    @Query("SELECT d FROM Delivery d WHERE d.user.deleted = false AND LOWER(d.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR LOWER(d.content) LIKE LOWER(CONCAT('%', :keyword, '%')) ORDER BY d.orderTime DESC")
    List<Delivery> searchByKeywordOrdered(@Param("keyword") String keyword);

}
