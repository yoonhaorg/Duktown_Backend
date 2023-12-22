package com.duktown.domain.chatRoom.service;

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

        Integer userNumber = chatRoomUserRepository.countByChatRoomId(chatRoom.getId());
        chatRoomUserRepository.save(request.toEntity(inviteUser, chatRoom, userNumber));
    }
}
