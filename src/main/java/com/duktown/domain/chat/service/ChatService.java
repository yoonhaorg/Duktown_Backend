package com.duktown.domain.chat.service;

import com.duktown.domain.chat.dto.ChatDto;
import com.duktown.domain.chat.entity.Chat;
import com.duktown.domain.chat.entity.ChatRepository;
import com.duktown.domain.chatRoom.entity.ChatRoom;
import com.duktown.domain.chatRoom.entity.ChatRoomRepository;
import com.duktown.domain.chatRoomUser.entity.ChatRoomUser;
import com.duktown.domain.chatRoomUser.entity.ChatRoomUserRepository;
import com.duktown.domain.user.entity.User;
import com.duktown.domain.user.entity.UserRepository;
import com.duktown.global.exception.CustomErrorType;
import com.duktown.global.exception.CustomException;
import com.duktown.global.type.ChatRoomUserType;
import com.duktown.global.type.ChatType;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static com.duktown.global.exception.CustomErrorType.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChatService {
    private final ChatRoomRepository chatRoomRepository;
    private final ChatRepository chatRepository;
    private final UserRepository userRepository;
    private final ChatRoomUserRepository chatRoomUserRepository;

    @Transactional
    public ChatDto.MessageResponse saveChat(Long chatRoomId, ChatDto.MessageRequest message) {
        ChatRoomUser chatRoomUser = chatRoomUserRepository.findByChatRoomIdAndUserId(chatRoomId, message.getUserId())
                .orElseThrow(() -> new CustomException(CHAT_ROOM_USER_NOT_FOUND));
        if (chatRoomUser.getChatRoomUserType() == ChatRoomUserType.DELETED) {
            throw new CustomException(CANNOT_SEND_WHEN_CHAT_ROOM_OWNER_EXIT);
        }

        User user = userRepository.findById(message.getUserId()).orElseThrow(() -> new CustomException(CustomErrorType.USER_NOT_FOUND));
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId).orElseThrow(() -> new CustomException(CHAT_ROOM_NOT_FOUND));
        ChatType chatType = ChatType.valueOf(message.getChatType());

        Chat chat = chatRepository.save(message.toEntity(user, chatRoom, chatType));

        return ChatDto.MessageResponse.from(chat, chatRoomUser);
    }

    public ChatDto.ListResponse getChatMessages(Long userId, Long chatRoomId, Pageable pageable) {
        // 사용자 존재 검증
        userRepository.findById(userId).orElseThrow(() -> new CustomException(CustomErrorType.USER_NOT_FOUND));
        // 채팅방 존재 검증
        chatRoomRepository.findById(chatRoomId).orElseThrow(() -> new CustomException(CHAT_ROOM_NOT_FOUND));

        // 해당 채팅방 안에 있는 유저인지 검증
        ChatRoomUser chatRoomUser = chatRoomUserRepository.findByChatRoomIdAndUserId(chatRoomId, userId)
                .orElseThrow(() -> new CustomException(CustomErrorType.CHAT_ROOM_USER_NOT_FOUND));

        if (chatRoomUser.getChatRoomUserType() == ChatRoomUserType.DELETED) {
            throw new CustomException(CustomErrorType.DELETED_CHAT_ROOM_USER);
        }

        Slice<Chat> chats;
        // 차단된 유저는 차단 이전 채팅 내역만 조회됨
        if (chatRoomUser.getChatRoomUserType() == ChatRoomUserType.BLOCKED) {
            chats = chatRepository.findSliceChatsForBlockedUser(chatRoomId, userId, pageable);
        } else {
            // 나가기한 유저가 아니면 채팅 내역 조회
            chats = chatRepository.findSliceChats(chatRoomId, userId, pageable);
        }

        List<ChatDto.MessageResponse> messages = chats.stream()
                .map(c -> {
                    Long chatUserId;
                    if (c.getUser() != null) {
                        chatUserId = c.getUser().getId();
                    } else {
                        chatUserId = null;
                    }

                    return ChatDto.MessageResponse.from(
                            c,
                            chatRoomUserRepository.findByChatRoomIdAndUserId(c.getChatRoom().getId(), chatUserId)
                                    .orElse(null));
                })
                .collect(Collectors.toList());
        return new ChatDto.ListResponse(messages);
    }
}
