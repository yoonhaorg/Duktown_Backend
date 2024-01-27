package com.duktown.domain.delivery.service;

import com.duktown.domain.chat.dto.ChatDto;
import com.duktown.domain.chat.entity.Chat;
import com.duktown.domain.chat.entity.ChatRepository;
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
import com.duktown.global.type.ChatType;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
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
    private final SimpMessagingTemplate simpMessagingTemplate;
    private final ChatRepository chatRepository;

    @Transactional
    public void createDelivery(Long userId, DeliveryDto.CreateRequest request) {
        if (request.getTitle().length() > 20) {
            throw new CustomException(DELIVERY_TITLE_TOO_LONG);
        }

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

        // 이미 마감된 배달팟은 또 모집종료할 수 없음
        if (!delivery.getActive()) {
            throw new CustomException(DELIVERY_ALREADY_CLOSED);
        }

        delivery.closeDelivery();

        // 모집 종료 시 배달팟 자동 삭제
        deleteDelivery(userId, deliveryId);

        String message = "글쓴이가 배달팟 인원 모집을 마감했습니다.";
        Chat chat = Chat.builder()
                .chatRoom(delivery.getChatRoom())
                .content(message)
                .chatType(ChatType.RECRUITMENT_FINISH)
                .build();

        chatRepository.save(chat);

        ChatDto.MessageResponse messageResponse = ChatDto.MessageResponse.from(chat, null);
        simpMessagingTemplate.convertAndSend("/sub/chatRoom/" + delivery.getChatRoom().getId(), messageResponse);
    }

    // 송금계좌 수정
    @Transactional
    public void updateAccountNumber(Long userId, Long deliveryId, DeliveryDto.AccountUpdateRequest request) {
        userRepository.findById(userId).orElseThrow(() -> new CustomException(USER_NOT_FOUND));
        ChatRoom chatRoom = chatRoomRepository.findByDeliveryId(deliveryId).orElseThrow(() -> new CustomException(CHAT_ROOM_NOT_FOUND));

        if (!userId.equals(chatRoom.getUser().getId())) {
            throw new CustomException(HAVE_NO_PERMISSION);
        }

        chatRoom.getDelivery().updateAccountNumber(seed.encrypt(request.getAccountNumber()));

        // 송금계좌 수정 알림
        String message = "글쓴이가 송금 계좌를 수정했어요!";

        Chat chat = Chat.builder()
                .chatRoom(chatRoom)
                .content(message)
                .chatType(ChatType.ACCOUNT_EDIT)
                .build();

        chatRepository.save(chat);

        ChatDto.MessageResponse messageResponse = ChatDto.MessageResponse.from(chat, null);
        simpMessagingTemplate.convertAndSend("/sub/chatRoom/" + chatRoom.getId(), messageResponse);
    }

    // 주문 완료
    @Transactional
    public void completeDelivery(Long userId, Long deliveryId) {
        userRepository.findById(userId).orElseThrow(() -> new CustomException(USER_NOT_FOUND));
        ChatRoom chatRoom = chatRoomRepository.findByDeliveryId(deliveryId).orElseThrow(() -> new CustomException(CHAT_ROOM_NOT_FOUND));

        if (!userId.equals(chatRoom.getUser().getId())) {
            throw new CustomException(HAVE_NO_PERMISSION);
        }

        // active가 true면 false로 변경
        if (chatRoom.getDelivery().getActive()) {
            chatRoom.getDelivery().closeDelivery();
        }

        // 주문 완료 알림
        String message = "글쓴이가 주문을 완료했어요!";

        Chat chat = Chat.builder()
                .chatRoom(chatRoom)
                .content(message)
                .chatType(ChatType.ORDER_FINISH)
                .build();

        chatRepository.save(chat);

        ChatDto.MessageResponse messageResponse = ChatDto.MessageResponse.from(chat, null);
        simpMessagingTemplate.convertAndSend("/sub/chatRoom/" + chatRoom.getId(), messageResponse);

        // 계좌번호 삭제 TODO: 필요에 따라 삭제
//        delivery.updateAccountNumber(seed.encrypt(""));
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

        List<DeliveryDto.DeliveryResponse> content = deliveries
                .stream()
                .map(d -> DeliveryDto.DeliveryResponse.from(d, d.getUser().getId().equals(userId),
                        chatRoomUserRepository.countByChatRoomId(d.getChatRoom().getId(), ChatRoomUserType.ACTIVE)))
                .collect(Collectors.toList());

        return new DeliveryDto.DeliveryListResponse(content);
    }

    // 상세 조회
    public DeliveryDto.DeliveryResponse getDeliveryDetail(Long userId, Long deliveryId) {
        userRepository.findById(userId).orElseThrow(() -> new CustomException(USER_NOT_FOUND));
        Delivery delivery = deliveryRepository.findById(deliveryId).orElseThrow(() -> new CustomException(DELIVERY_NOT_FOUND));

        // 글쓴이인지 여부
        Boolean isWriter = delivery.getUser().getId().equals(userId);
        Integer peopleCnt = chatRoomUserRepository.countByChatRoomId(delivery.getChatRoom().getId(), ChatRoomUserType.ACTIVE);

        return DeliveryDto.DeliveryResponse.from(delivery, isWriter, peopleCnt);
    }

    // 삭제
    @Transactional
    public void deleteDelivery(Long userId, Long deliveryId) {
        userRepository.findById(userId).orElseThrow(() -> new CustomException(USER_NOT_FOUND));
        Delivery delivery = deliveryRepository.findById(deliveryId).orElse(null);

        // 배달팟이 null이면 이미 삭제된 것이므로 삭제 과정 x
        if (delivery != null) {
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

    // 검색
    public DeliveryDto.DeliveryListResponse searchDeliveryList(Long userId, String keyword) {
        userRepository.findById(userId).orElseThrow(() -> new CustomException(USER_NOT_FOUND));

        List<Delivery> deliveries = deliveryRepository.searchByKeywordOrdered(keyword);

        List<DeliveryDto.DeliveryResponse> content = deliveries
                .stream()
                .map(d -> DeliveryDto.DeliveryResponse.from(d, d.getUser().getId().equals(userId),
                        chatRoomUserRepository.countByChatRoomId(d.getChatRoom().getId(), ChatRoomUserType.ACTIVE)))
                .collect(Collectors.toList());

        return new DeliveryDto.DeliveryListResponse(content);
    }
}
