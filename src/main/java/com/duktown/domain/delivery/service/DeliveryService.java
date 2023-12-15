package com.duktown.domain.delivery.service;

import com.duktown.domain.chatRoom.entity.ChatRoom;
import com.duktown.domain.chatRoom.entity.ChatRoomRepository;
import com.duktown.domain.chatRoomUser.entity.ChatRoomUser;
import com.duktown.domain.chatRoomUser.entity.ChatRoomUserRepository;
import com.duktown.domain.delivery.dto.DeliveryDto;
import com.duktown.domain.delivery.entity.Delivery;
import com.duktown.domain.delivery.entity.DeliveryRepository;
import com.duktown.domain.user.entity.User;
import com.duktown.domain.user.entity.UserRepository;
import com.duktown.global.exception.CustomException;
import com.duktown.global.kisa_SEED.SEED;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.duktown.global.exception.CustomErrorType.USER_NOT_FOUND;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DeliveryService {
    private final UserRepository userRepository;
    private final DeliveryRepository deliveryRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final ChatRoomUserRepository chatRoomUserRepository;
    private final SEED seed;

    @Transactional
    public void createDelivery(Long userId, DeliveryDto.CreateRequest request) {
        User user = userRepository.findById(userId).orElseThrow(() -> new CustomException(USER_NOT_FOUND));
        // 배달팟 생성
        Delivery delivery = deliveryRepository.save(request.toEntity(user, seed.encrypt(request.getAccountNumber())));
        // 채팅방 동시 생성 (채팅방 이름은 배달팟 이름)
        ChatRoom chatRoom = chatRoomRepository.save(
                ChatRoom.builder()
                        .user(user)
                        .delivery(delivery)
                        .name(request.getTitle())
                        .build());

        // 채팅방에 방장 입장
        chatRoomUserRepository.save(
                ChatRoomUser.builder()
                        .user(user)
                        .chatRoom(chatRoom)
                        .userNumber(0)  // 방장은 0
                        .build()
        );
    }
}
