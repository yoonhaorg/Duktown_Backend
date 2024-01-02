package com.duktown.domain.chatRoom.dto;

import com.duktown.domain.chatRoom.entity.ChatRoom;
import com.duktown.domain.delivery.entity.Delivery;
import com.duktown.global.kisa_SEED.SEED;
import com.duktown.global.util.DateUtil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

public class ChatRoomDto {

    @Getter
    @Builder
    @AllArgsConstructor
    public static class ChatRoomResponse {
        private Long chatRoomId;
        private Long deliveryId;
        private String title;
        private Integer maxPeople;
        private String orderTime;
        private String accountNumber;

        public static ChatRoomResponse from(Delivery delivery, String accountNumber) {
            return ChatRoomResponse.builder()
                    .chatRoomId(delivery.getChatRoom().getId())
                    .deliveryId(delivery.getId())
                    .title(delivery.getTitle())
                    .maxPeople(delivery.getMaxPeople())
                    .orderTime(DateUtil.convertToAMPMFormat(delivery.getOrderTime()))
                    .accountNumber(accountNumber)
                    .build();
        }
    }

    @Getter
    @Builder
    @AllArgsConstructor
    public static class ChatRoomListResponse {
        private List<ChatRoomResponse> chatRooms;

        public static ChatRoomListResponse from(List<ChatRoom> chatRooms) {
            return ChatRoomListResponse.builder()
                    .chatRooms(chatRooms
                            .stream()
                            .map(c -> ChatRoomResponse.from(c.getDelivery(), c.getDelivery().getAccountNumber()))
                            .collect(Collectors.toList())
                    )
                    .build();
        }
    }
}
