package com.duktown.domain.delivery.service;

import com.duktown.domain.chatRoom.entity.ChatRoom;
import com.duktown.domain.chatRoom.entity.ChatRoomRepository;
import com.duktown.domain.chatRoomUser.entity.ChatRoomUser;
import com.duktown.domain.chatRoomUser.entity.ChatRoomUserRepository;
import com.duktown.domain.comment.entity.Comment;
import com.duktown.domain.comment.entity.CommentRepository;
import com.duktown.domain.delivery.dto.DeliveryDto;
import com.duktown.domain.delivery.entity.Delivery;
import com.duktown.domain.delivery.entity.DeliveryRepository;
import com.duktown.domain.user.entity.User;
import com.duktown.domain.user.entity.UserRepository;
import com.duktown.global.exception.CustomException;
import com.duktown.global.kisa_SEED.SEED;
import com.duktown.global.type.ChatRoomUserType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static com.duktown.global.exception.CustomErrorType.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DeliveryService {
    private final UserRepository userRepository;
    private final DeliveryRepository deliveryRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final ChatRoomUserRepository chatRoomUserRepository;
    private final SEED seed;
    private final CommentRepository commentRepository;

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
                        .chatRoomUserType(ChatRoomUserType.ACTIVE)
                        .build()
        );
    }

    // 모집 종료
    @Transactional
    public void closeDelivery(Long userId, Long deliveryId) {
        userRepository.findById(userId).orElseThrow(() -> new CustomException(USER_NOT_FOUND));
        Delivery delivery = deliveryRepository.findById(deliveryId).orElseThrow(() -> new CustomException(DELIVERY_NOT_FOUND));

        if (!userId.equals(delivery.getUser().getId())) {
            throw new CustomException(HAVE_NO_PERMISSION);
        }

        delivery.closeDelivery();
    }

    // 송금계좌 수정
    @Transactional
    public void updateAccountNumber(Long userId, Long deliveryId, DeliveryDto.AccountUpdateRequest request) {
        userRepository.findById(userId).orElseThrow(() -> new CustomException(USER_NOT_FOUND));
        Delivery delivery = deliveryRepository.findById(deliveryId).orElseThrow(() -> new CustomException(DELIVERY_NOT_FOUND));

        if (!userId.equals(delivery.getUser().getId())) {
            throw new CustomException(HAVE_NO_PERMISSION);
        }

        delivery.updateAccountNumber(seed.encrypt(request.getAccountNumber()));
    }

    // 주문 완료
    @Transactional
    public void completeDelivery(Long userId, Long deliveryId) {
        userRepository.findById(userId).orElseThrow(() -> new CustomException(USER_NOT_FOUND));
        Delivery delivery = deliveryRepository.findById(deliveryId).orElseThrow(() -> new CustomException(DELIVERY_NOT_FOUND));

        if (!userId.equals(delivery.getUser().getId())) {
            throw new CustomException(HAVE_NO_PERMISSION);
        }

        // active가 true면 false로 변경
        if (delivery.getActive()) {
            delivery.closeDelivery();
        }

        // 계좌번호 삭제
        delivery.updateAccountNumber(seed.encrypt(""));
    }

    // 목록 조회
    public DeliveryDto.DeliveryListResponse getDeliveryList(Long userId, Integer sortBy) {
        userRepository.findById(userId).orElseThrow(() -> new CustomException(USER_NOT_FOUND));

        List<Delivery> deliveries;

        // deleted = false인 배달팟만 조회됨
        if(sortBy == null || sortBy == 0){  // 최신순 정렬
            deliveries = deliveryRepository.findAllSortByCreatedAt();
        } else if (sortBy == 1) {   // 주문시간순 정렬
            deliveries = deliveryRepository.findAllSortByOrderTime();
        } else {
            throw new CustomException(INVALID_DELIVERY_SORTBY_VALUE);
        }

        return DeliveryDto.DeliveryListResponse.from(deliveries);
    }

    // 상세 조회
    public DeliveryDto.DeliveryResponse getDeliveryDetail(Long userId, Long deliveryId) {
        userRepository.findById(userId).orElseThrow(() -> new CustomException(USER_NOT_FOUND));
        Delivery delivery = deliveryRepository.findById(deliveryId).orElseThrow(() -> new CustomException(DELIVERY_NOT_FOUND));

        // deleted = true면 조회 x
        if (delivery.getDeleted()) {
            throw new CustomException(DELIVERY_NOT_FOUND);
        }

        return DeliveryDto.DeliveryResponse.from(delivery);
    }

    // 삭제
    @Transactional
    public void deleteDelivery(Long userId, Long deliveryId) {
        userRepository.findById(userId).orElseThrow(() -> new CustomException(USER_NOT_FOUND));
        Delivery delivery = deliveryRepository.findById(deliveryId).orElseThrow(() -> new CustomException(DELIVERY_NOT_FOUND));

        if (!userId.equals(delivery.getUser().getId())) {
            throw new CustomException(HAVE_NO_PERMISSION);
        }

        // 댓글 먼저 삭제
        List<Comment> comments = commentRepository.findAllByDeliveryId(delivery.getId());
        if (comments != null) {
            List<Long> commentIds = comments.stream().map(Comment::getId).collect(Collectors.toList());
            commentRepository.deleteAllById(commentIds);
        }

        // 배달팟 삭제(deleted = true)
        delivery.delete();
    }
}
