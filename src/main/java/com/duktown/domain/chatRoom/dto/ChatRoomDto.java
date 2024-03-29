package com.duktown.domain.chatRoom.dto;

import com.duktown.domain.chatRoom.entity.ChatRoom;
import com.duktown.domain.chatRoomUser.entity.ChatRoomUser;
import com.duktown.domain.delivery.entity.Delivery;
import com.duktown.global.util.DateUtil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

public class ChatRoomDto {

    @Getter
    @Builder
    @AllArgsConstructor
    public static class ChatRoomResponse {
        private Long getRequestUserId;
        private Integer getRequestUserNumber;
        private Long chatRoomId;
        private Long deliveryId;
        private Boolean deliveryDeleted;
        private String title;
        private Integer maxPeople;
        private Integer chatRoomUserCnt;
        private String orderTime;
        private String accountNumber;

        public static ChatRoomResponse from(ChatRoomUser chatRoomUser, Delivery delivery, String accountNumber, Integer chatRoomUserCnt) {
            return ChatRoomResponse.builder()
                    .getRequestUserId(chatRoomUser.getUser().getId())
                    .getRequestUserNumber(chatRoomUser.getUserNumber())
                    .chatRoomId(delivery.getChatRoom().getId())
                    .deliveryId(delivery.getId())
                    .deliveryDeleted(delivery.getDeleted())
                    .title(delivery.getTitle())
                    .maxPeople(delivery.getMaxPeople())
                    .chatRoomUserCnt(chatRoomUserCnt)
                    .orderTime(DateUtil.convertToAMPMFormat(delivery.getOrderTime()))
                    .accountNumber(accountNumber)
                    .build();
        }
    }

    @Getter
    @Builder
    @AllArgsConstructor
    public static class ChatRoomListElementResponse {   // TODO: 읽지 않은 알림 개수
        private Long chatRoomId;
        private String title;
        private String recentChatMessage;
        private LocalDateTime recentChatCreatedAt;

        public static ChatRoomListElementResponse from(ChatRoom chatRoom, String recentChatContent, LocalDateTime recentChatCreatedAt) {
            return ChatRoomListElementResponse.builder()
                    .chatRoomId(chatRoom.getId())
                    .title(chatRoom.getDelivery().getTitle())
                    .recentChatMessage(recentChatContent)
                    .recentChatCreatedAt(recentChatCreatedAt)
                    .build();
        }
    }

    @Getter
    public static class ChatRoomListResponse {
        private final List<ChatRoomListElementResponse> chatRooms;

        public ChatRoomListResponse(List<ChatRoomListElementResponse> chatRooms) {
            this.chatRooms = chatRooms;
        }
    }
}
