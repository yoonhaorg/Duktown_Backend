package com.duktown.domain.chat.entity;

import com.duktown.domain.chatRoom.entity.ChatRoom;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ChatRepository extends JpaRepository<Chat, Long> {

    @Query(value = "select c from Chat c " +
            "where c.chatRoom.id = :chat_room_id " +
            "and c.createdAt > (" +
            "select cru.createdAt from ChatRoomUser cru " +
            "where cru.chatRoom.id = :chat_room_id and " +
            "cru.user.id = :user_id" +
            ") order by c.createdAt desc")
    Slice<Chat> findSliceChats(@Param("chat_room_id") Long chatRoomId,
                               @Param("user_id") Long userId,
                               Pageable pageable);

    @Query(value = "select c from Chat c " +
            "where c.chatRoom.id = :chat_room_id " +
            "and c.createdAt > (" +
            "select cru.createdAt from ChatRoomUser cru " +
            "where cru.chatRoom.id = :chat_room_id and " +
            "cru.user.id = :user_id) " +
            "and c.createdAt < (" +
            "select cru.modifiedAt from ChatRoomUser cru " +
            "where cru.chatRoom.id = :chat_room_id and " +
            "cru.user.id = :user_id) " +
            "order by c.createdAt desc")
    Slice<Chat> findSliceChatsForBlockedUser(@Param("chat_room_id") Long chatRoomId,
                               @Param("user_id") Long userId,
                               Pageable pageable);

    Optional<Chat> findTopByChatRoomOrderByCreatedAtDesc(ChatRoom chatRoom);
}
