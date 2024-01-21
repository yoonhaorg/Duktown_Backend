package com.duktown.domain.chatRoomUser.entity;

import com.duktown.global.type.ChatRoomUserType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChatRoomUserRepository extends JpaRepository<ChatRoomUser, Long> {

    Boolean existsByChatRoomIdAndUserId(Long chatRoomId, Long userId);

    Boolean existsByChatRoomIdAndUserNumber(Long chatRoomId, Integer userNumber);

    Optional<ChatRoomUser> findByChatRoomIdAndUserId(Long chatRoomId, Long userId);

    List<ChatRoomUser> findAllByUserId(Long userId);

    @Query("select count(cru) from ChatRoomUser cru where cru.chatRoom.id = :chatRoomId and cru.chatRoomUserType = :chatRoomUserType")
    Integer countByChatRoomId(@Param("chatRoomId") Long chatRoomId, @Param("chatRoomUserType")ChatRoomUserType chatRoomUserType);
}
