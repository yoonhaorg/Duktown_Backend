package com.duktown.domain.chatRoomUser.entity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatRoomUserRepository extends JpaRepository<ChatRoomUser, Long> {

    @Query(value = "select count(*) from chat_room where chat_room_id = :chat_room_id", nativeQuery = true)
    Integer countByChatRoomId(@Param("chat_room_id") Long chatRoomId);
}
