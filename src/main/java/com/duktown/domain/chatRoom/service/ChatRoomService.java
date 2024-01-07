package com.duktown.domain.chatRoom.service;

import com.duktown.domain.chat.dto.ChatDto;
import com.duktown.domain.chat.entity.Chat;
import com.duktown.domain.chat.entity.ChatRepository;
import com.duktown.domain.chatRoom.dto.ChatRoomDto;
import com.duktown.domain.chatRoom.entity.ChatRoom;
import com.duktown.domain.chatRoom.entity.ChatRoomRepository;
import com.duktown.domain.chatRoomUser.dto.ChatRoomUserDto;
import com.duktown.domain.chatRoomUser.entity.ChatRoomUser;
import com.duktown.domain.chatRoomUser.entity.ChatRoomUserRepository;
import com.duktown.domain.delivery.entity.Delivery;
import com.duktown.domain.delivery.entity.DeliveryRepository;
import com.duktown.domain.user.entity.User;
import com.duktown.domain.user.entity.UserRepository;
import com.duktown.global.exception.CustomErrorType;
import com.duktown.global.exception.CustomException;
import com.duktown.global.kisa_SEED.SEED;
import com.duktown.global.type.ChatRoomUserType;
import com.duktown.global.type.ChatType;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static com.duktown.global.exception.CustomErrorType.CHAT_ROOM_USER_NOT_FOUND;
import static com.duktown.global.exception.CustomErrorType.USER_NOT_FOUND;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChatRoomService {

    private final UserRepository userRepository;
    private final DeliveryRepository deliveryRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final ChatRoomUserRepository chatRoomUserRepository;
    private final SEED seed;
    private final SimpMessagingTemplate simpMessagingTemplate;
    private final ChatRepository chatRepository;

    // 배달팟 채팅방 초대하기
    @Transactional
    public void inviteChatRoomUser(Long userId, ChatRoomUserDto.InviteRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(USER_NOT_FOUND));

        // 자기 자신은 초대 불가
        if (user.getId().equals(request.getInviteUserId())) {
            throw new CustomException(CustomErrorType.CANNOT_INVITE_SELF);
        }

        Delivery delivery = deliveryRepository.findById(request.getDeliveryId()).orElseThrow(() -> new CustomException(CustomErrorType.DELIVERY_NOT_FOUND));
        ChatRoom chatRoom = chatRoomRepository.findById(delivery.getChatRoom().getId()).orElseThrow(() -> new CustomException(CustomErrorType.CHAT_ROOM_NOT_FOUND));

        // 배달팟이 활성 상태여야만 초대 가능
        if (!delivery.getActive()) {
            throw new CustomException(CustomErrorType.DELIVERY_ALREADY_CLOSED);
        }

        // 채팅방 주인만 초대 가능
        if (!user.getId().equals(chatRoom.getUser().getId())) {
            throw new CustomException(CustomErrorType.NO_PERMISSION_TO_INVITE_CHAT_ROOM);
        }

        User inviteUser = userRepository.findById(request.getInviteUserId()).orElseThrow(() -> new CustomException(USER_NOT_FOUND));

        ChatRoomUser chatRoomUser;

        // 이미 초대된 적이 있었던 유저인지 확인
        if (chatRoomUserRepository.existsByChatRoomIdAndUserId(chatRoom.getId(), request.getInviteUserId())) {
            chatRoomUserRepository.findByChatRoomIdAndUserId(chatRoom.getId(), request.getInviteUserId())
                    .ifPresent(c -> {
                        // 이미 초대된 유저는 초대 불가(ACTIVE인 경우)
                        if(c.getChatRoomUserType() == ChatRoomUserType.ACTIVE) {
                            throw new CustomException(CustomErrorType.USER_ALREADY_EXISTS_IN_CHAT_ROOM);
                        }
                        // 차단된 유저라면 초대 불가
                        else if (c.getChatRoomUserType() == ChatRoomUserType.BLOCKED) {
                            throw new CustomException(CustomErrorType.BLOCKED_CHAT_ROOM_USER);
                        }
                        // 나가기했던 유저라면 ACTIVE로 재활성화, createdAt 현재로 변경
                        else {
                            c.changeChatRoomUserType(ChatRoomUserType.ACTIVE);
                            c.setCreatedAt(LocalDateTime.now());
                        }
                    });

            chatRoomUser = chatRoomUserRepository.findByChatRoomIdAndUserId(chatRoom.getId(), request.getInviteUserId())
                    .orElseThrow(() -> new CustomException(CHAT_ROOM_USER_NOT_FOUND));
        }

        // 초대된 적 없다면 chatRoomUser 등록
        else {
            chatRoomUser = chatRoomUserRepository.save(request.toEntity(inviteUser, chatRoom));
        }

        String message = "익명" + chatRoomUser.getUserNumber() + "님이 들어왔습니다.";

        Chat chat = Chat.builder()
                .chatRoom(chatRoom)
                .content(message)
                .chatType(ChatType.JOIN)
                .build();

        chatRepository.save(chat);

        ChatDto.MessageResponse messageResponse = ChatDto.MessageResponse.from(chat, null);
        simpMessagingTemplate.convertAndSend("/sub/chatRoom/" + chatRoom.getId(), messageResponse);
    }

    // 채팅방 조회
    public ChatRoomDto.ChatRoomResponse getChatRoom(Long userId, Long chatRoomId) {
        userRepository.findById(userId).orElseThrow(() -> new CustomException(USER_NOT_FOUND));
        chatRoomRepository.findById(chatRoomId).orElseThrow(() -> new CustomException(CustomErrorType.CHAT_ROOM_NOT_FOUND));

        // 해당 채팅방 안에 있는 유저인지 검증
        if (!chatRoomUserRepository.existsByChatRoomIdAndUserId(chatRoomId, userId)) {
            chatRoomUserRepository.findByChatRoomIdAndUserId(chatRoomId, userId)
                    .ifPresent(c -> {
                        // 차단된 사용자라면 조회 불가능
                        if(c.getChatRoomUserType() == ChatRoomUserType.BLOCKED) {
                            throw new CustomException(CustomErrorType.BLOCKED_FROM_CHAT_ROOM);
                        } else if (c.getChatRoomUserType() == ChatRoomUserType.DELETED) {
                            throw new CustomException(CustomErrorType.DELETED_CHAT_ROOM_USER);
                        } else {
                            throw new CustomException(CustomErrorType.CHAT_ROOM_USER_NOT_FOUND);
                        }
                    });
        }

        Delivery delivery = deliveryRepository.findByChatRoomId(chatRoomId).orElseThrow(() -> new CustomException(CustomErrorType.DELIVERY_NOT_FOUND));

        return ChatRoomDto.ChatRoomResponse.from(delivery, seed.decrypt(delivery.getAccountNumber()));
    }

    // 내가 참여중인 채팅방 목록 조회 TODO : 페이징 처리, 안 읽은 개수 표시
    public ChatRoomDto.ChatRoomListResponse getChatRoomList(Long userId) {
        userRepository.findById(userId).orElseThrow(() -> new CustomException(USER_NOT_FOUND));
        List<ChatRoomUser> chatRoomUsers = chatRoomUserRepository.findAllByUserId(userId);
        List<ChatRoomDto.ChatRoomListElementResponse> chatRooms = new ArrayList<>();

        for (ChatRoomUser chatRoomUser : chatRoomUsers) {
            // 참여중인 채팅방 찾기
            ChatRoom chatRoom = chatRoomUser.getChatRoom();

            // 가장 최근 메시지 찾기
            Chat recentChat;
            String recentChatContent;
            LocalDateTime recentChatCreatedAt;
            if (chatRoomUser.getChatRoomUserType() == ChatRoomUserType.BLOCKED) {
                recentChat = chatRepository.findSliceChatsForBlockedUser(chatRoom.getId(), chatRoomUser.getUser().getId(), PageRequest.of(0, 1))
                        .toList().get(0);
            } else {
                recentChat = chatRepository.findTopByChatRoomOrderByCreatedAtDesc(chatRoom)
                        .orElse(null);
            }

            if (recentChat == null) {
                recentChatContent = "아직 채팅이 없습니다.";
                recentChatCreatedAt = chatRoom.getCreatedAt();
            } else {
                recentChatContent = recentChat.getContent();
                recentChatCreatedAt = recentChat.getCreatedAt();
            }

            chatRooms.add(ChatRoomDto.ChatRoomListElementResponse.from(chatRoom, recentChatContent, recentChatCreatedAt));
        }

        chatRooms.sort(Comparator.comparing(ChatRoomDto.ChatRoomListElementResponse::getRecentChatCreatedAt).reversed());

        return new ChatRoomDto.ChatRoomListResponse(chatRooms);
    }

    // 채팅방 나가기
    @Transactional
    public void exitChatRoom(Long userId, Long chatRoomId) {
        userRepository.findById(userId).orElseThrow(() -> new CustomException(USER_NOT_FOUND));
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId).orElseThrow(() -> new CustomException(CustomErrorType.CHAT_ROOM_NOT_FOUND));

        ChatRoomUser chatRoomUser = chatRoomUserRepository.findByChatRoomIdAndUserId(chatRoomId, userId)
                .orElseThrow(() -> new CustomException(CustomErrorType.CHAT_ROOM_USER_NOT_FOUND));

        // 차단된 유저는 나가기해도 채팅방을 나갔습니다가 보내지면 안 된다
        if (chatRoomUser.getChatRoomUserType() == ChatRoomUserType.BLOCKED) {
            return;
        }

        // 상태 DELETED로 변경
        chatRoomUser.changeChatRoomUserType(ChatRoomUserType.DELETED);

        Integer userNumber = chatRoomUser.getUserNumber();
        String message;
        ChatType chatType;
        if (userNumber == 0) {
            message = "글쓴이가 채팅방을 나갔습니다. 더 이상 채팅을 전송할 수 없습니다.";
            chatType = ChatType.WRITER_EXIT;
        } else {
            message = "익명" + userNumber + "님이 채팅방을 나갔습니다.";
            chatType = ChatType.EXIT;
        }

        Chat chat = Chat.builder()
                .chatRoom(chatRoom)
                .content(message)
                .chatType(chatType)
                .build();

        chatRepository.save(chat);

        ChatDto.MessageResponse messageResponse = ChatDto.MessageResponse.from(chat, null);
        simpMessagingTemplate.convertAndSend("/sub/chatRoom/" + chatRoom.getId(), messageResponse);
    }
}
