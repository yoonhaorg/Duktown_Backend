package com.duktown.domain.chatRoomUser.service;

import com.duktown.domain.chat.dto.ChatDto;
import com.duktown.domain.chat.entity.Chat;
import com.duktown.domain.chat.entity.ChatRepository;
import com.duktown.domain.chatRoom.entity.ChatRoom;
import com.duktown.domain.chatRoom.entity.ChatRoomRepository;
import com.duktown.domain.chatRoomUser.dto.ChatRoomUserDto;
import com.duktown.domain.chatRoomUser.entity.ChatRoomUser;
import com.duktown.domain.chatRoomUser.entity.ChatRoomUserRepository;
import com.duktown.domain.user.entity.UserRepository;
import com.duktown.global.exception.CustomErrorType;
import com.duktown.global.exception.CustomException;
import com.duktown.global.type.ChatRoomUserType;
import com.duktown.global.type.ChatType;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChatRoomUserService {

    private final UserRepository userRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final ChatRoomUserRepository chatRoomUserRepository;
    private final ChatRepository chatRepository;
    private final SimpMessagingTemplate simpMessagingTemplate;

    // 채팅방에서 사용자 내보내기
    @Transactional
    public void blockChatRoomUser(Long userId, ChatRoomUserDto.BlockRequest request) {
        userRepository.findById(userId).orElseThrow(() -> new CustomException(CustomErrorType.USER_NOT_FOUND));
        ChatRoom chatRoom = chatRoomRepository.findById(request.getChatRoomId()).orElseThrow(() -> new CustomException(CustomErrorType.CHAT_ROOM_NOT_FOUND));

        // 채팅방 주인이 아니면 차단 불가
        if(!chatRoom.getUser().getId().equals(userId)) {
            throw new CustomException(CustomErrorType.NO_PERMISSION_TO_BLOCK_CHAT_ROOM_USER);
        }

        // 자기 자신은 차단 불가
        if (userId.equals(request.getBlockUserId())) {
            throw new CustomException(CustomErrorType.CANNOT_BLOCK_SELF);
        }

        ChatRoomUser chatRoomUser = chatRoomUserRepository.findByChatRoomIdAndUserId(request.getChatRoomId(), request.getBlockUserId())
                .orElseThrow(() -> new CustomException(CustomErrorType.CHAT_ROOM_NOT_FOUND));

        chatRoomUser.changeChatRoomUserType(ChatRoomUserType.BLOCKED);

        String message = "글쓴이가 익명" + chatRoomUser.getUserNumber() + "님을 내보냈습니다.";

        Chat chat = Chat.builder()
                .chatRoom(chatRoom)
                .content(message)
                .chatType(ChatType.FORCE_OUT)
                .build();

        chatRepository.save(chat);

        ChatDto.MessageResponse messageResponse = ChatDto.MessageResponse.from(chat, null);
        simpMessagingTemplate.convertAndSend("/sub/chatRoom/" + chatRoom.getId(), messageResponse);
    }

}
