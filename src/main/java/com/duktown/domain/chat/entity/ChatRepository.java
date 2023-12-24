package com.duktown.domain.chat.entity;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatRepository extends JpaRepository<Chat, Long> {

    Slice<Chat> findSliceByChatRoomId(Long chatRoomId, Pageable pageable);
}
