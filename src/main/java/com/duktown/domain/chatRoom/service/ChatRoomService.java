package com.duktown.domain.chatRoom.service;

import com.duktown.domain.chatRoom.dto.ChatRoomDto;
import com.duktown.domain.chatRoom.entity.ChatRoom;
import com.duktown.domain.chatRoom.entity.ChatRoomRepository;
import com.duktown.domain.chatRoomUser.dto.ChatRoomUserDto;
import com.duktown.domain.chatRoomUser.entity.ChatRoomUserRepository;
import com.duktown.domain.delivery.entity.Delivery;
import com.duktown.domain.delivery.entity.DeliveryRepository;
import com.duktown.domain.user.entity.User;
import com.duktown.domain.user.entity.UserRepository;
import com.duktown.global.exception.CustomErrorType;
import com.duktown.global.exception.CustomException;
import com.duktown.global.kisa_SEED.SEED;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChatRoomService {

    private final UserRepository userRepository;
    private final DeliveryRepository deliveryRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final ChatRoomUserRepository chatRoomUserRepository;
    private final SEED seed;

    // 배달팟 채팅방 초대하기
    @Transactional
    public void inviteChatRoomUser(Long userId, ChatRoomUserDto.InviteRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(CustomErrorType.USER_NOT_FOUND));

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

        User inviteUser = userRepository.findById(request.getInviteUserId()).orElseThrow(() -> new CustomException(CustomErrorType.USER_NOT_FOUND));

        // 이미 초대된 유저는 초대 불가
        if (chatRoomUserRepository.existsByChatRoomIdAndUserId(chatRoom.getId(), request.getInviteUserId())) {
            throw new CustomException(CustomErrorType.USER_ALREADY_EXISTS_IN_CHAT_ROOM);
        }

        // 가장 최근에 채팅방에 참여한 사람의 userNumber에서 1 더한 값 TODO: BlockedUser, ActivatedUser, DeletedUser로 관리
        Integer userNumber = chatRoomUserRepository.countByChatRoomId(chatRoom.getId());
        chatRoomUserRepository.save(request.toEntity(inviteUser, chatRoom, userNumber));
    }

    // 채팅방 조회
    public ChatRoomDto.ChatRoomResponse getChatRoom(Long userId, Long chatRoomId) {
        userRepository.findById(userId).orElseThrow(() -> new CustomException(CustomErrorType.USER_NOT_FOUND));
        chatRoomRepository.findById(chatRoomId).orElseThrow(() -> new CustomException(CustomErrorType.CHAT_ROOM_NOT_FOUND));

        // 해당 채팅방 안에 있는 유저인지 검증
        if (!chatRoomUserRepository.existsByChatRoomIdAndUserId(chatRoomId, userId)) {
            throw new CustomException(CustomErrorType.USER_NOT_EXISTS_IN_CHAT_ROOM);
        }

        Delivery delivery = deliveryRepository.findByChatRoomId(chatRoomId).orElseThrow(() -> new CustomException(CustomErrorType.DELIVERY_NOT_FOUND));

        return ChatRoomDto.ChatRoomResponse.from(delivery, seed.decrypt(delivery.getAccountNumber()));
    }

    // 채팅방 나가기
    @Transactional
    public void exitChatRoom(Long userId, Long chatRoomId) {
        userRepository.findById(userId).orElseThrow(() -> new CustomException(CustomErrorType.USER_NOT_FOUND));
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId).orElseThrow(() -> new CustomException(CustomErrorType.CHAT_ROOM_NOT_FOUND));

        // 채팅방 주인은 채팅방 나가기 불가
        if (!chatRoom.getUser().getId().equals(userId)) {
            throw new CustomException(CustomErrorType.CHAT_ROOM_OWNER_CANNOT_EXIT);
        }

        // 해당 채팅방 안에 있는 유저인지 검증
        if (!chatRoomUserRepository.existsByChatRoomIdAndUserId(chatRoomId, userId)) {
            throw new CustomException(CustomErrorType.USER_NOT_EXISTS_IN_CHAT_ROOM);
        }

        chatRoomUserRepository.deleteByUserIdAndChatRoomId(userId, chatRoomId);
    }
}
