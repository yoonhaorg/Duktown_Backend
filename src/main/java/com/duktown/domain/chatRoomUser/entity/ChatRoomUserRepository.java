package com.duktown.domain.chatRoomUser.entity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChatRoomUserRepository extends JpaRepository<ChatRoomUser, Long> {

    Integer countByChatRoomId(Long chatRoomId);

    Boolean existsByChatRoomIdAndUserId(Long chatRoomId, Long userId);

    Optional<ChatRoomUser> findByChatRoomIdAndUserId(Long chatRoomId, Long userId);

    List<ChatRoomUser> findAllByUserId(Long userId);
}
