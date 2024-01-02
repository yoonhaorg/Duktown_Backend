package com.duktown.domain.chatRoom.service;

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
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

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
                        // 나가기했던 유저라면 ACTIVE로 재활성화
                        else {
                            c.changeChatRoomUserType(ChatRoomUserType.ACTIVE);
                        }
                    });

            chatRoomUser = chatRoomUserRepository.findByChatRoomIdAndUserId(chatRoom.getId(), request.getInviteUserId())
                    .orElseThrow(() -> new CustomException(CHAT_ROOM_USER_NOT_FOUND));
        }

        // 초대된 적 없다면 chatRoomUser 등록
        else {
            Integer userNumber = chatRoomUserRepository.countByChatRoomId(chatRoom.getId());
            chatRoomUser = chatRoomUserRepository.save(request.toEntity(inviteUser, chatRoom, userNumber));
        }

        simpMessagingTemplate.convertAndSend("/sub/chatRoom/" + chatRoom.getId(),
                "익명" + chatRoomUser.getUserNumber() + "님이 들어왔습니다.");
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

    // 내가 참여중인 채팅방 목록 조회 TODO : 채팅 온 순으로 자동 정렬
    public ChatRoomDto.ChatRoomListResponse getChatRoomList(Long userId) {
        userRepository.findById(userId).orElseThrow(() -> new CustomException(USER_NOT_FOUND));
        List<ChatRoomUser> chatRoomUsers = chatRoomUserRepository.findAllByUserId(userId);
        if (chatRoomUsers != null) {
            List<ChatRoom> chatRooms = chatRoomUsers.stream().map(ChatRoomUser::getChatRoom).collect(Collectors.toList());
            return ChatRoomDto.ChatRoomListResponse.from(chatRooms);
        }

        return null;
    }

    // 채팅방 나가기
    @Transactional
    public void exitChatRoom(Long userId, Long chatRoomId) {
        userRepository.findById(userId).orElseThrow(() -> new CustomException(USER_NOT_FOUND));
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId).orElseThrow(() -> new CustomException(CustomErrorType.CHAT_ROOM_NOT_FOUND));

        ChatRoomUser chatRoomUser = chatRoomUserRepository.findByChatRoomIdAndUserId(chatRoomId, userId)
                .orElseThrow(() -> new CustomException(CustomErrorType.CHAT_ROOM_USER_NOT_FOUND));

        // 상태 DELETED로 변경
        chatRoomUser.changeChatRoomUserType(ChatRoomUserType.DELETED);

        Integer userNumber = chatRoomUser.getUserNumber();
        String userName;
        if (userNumber == 0) {
            userName = "글쓴이";
        } else {
            userName = "익명" + userNumber;
        }

        simpMessagingTemplate.convertAndSend("/sub/chatRoom/" + chatRoom.getId(),
                userName + "님이 채팅방을 나갔습니다.");
    }
}
