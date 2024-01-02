package com.duktown.domain.delivery.entity;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DeliveryRepository extends JpaRepository<Delivery, Long> {

    Optional<Delivery> findByChatRoomId(Long chatRoomId);

    @Query(value = "select d from Delivery d where d.deleted = false order by d.createdAt desc")
    List<Delivery> findAllSortByCreatedAt();

    @Query(value = "select d from Delivery d where d.deleted = false order by d.orderTime")
    List<Delivery> findAllSortByOrderTime();
}
