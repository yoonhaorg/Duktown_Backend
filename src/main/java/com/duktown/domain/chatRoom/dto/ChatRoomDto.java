package com.duktown.domain.chatRoom.dto;

import com.duktown.domain.delivery.entity.Delivery;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

public class ChatRoomDto {

    @Getter
    @Builder
    @AllArgsConstructor
    public static class ChatRoomResponse {
        private Long chatRoomId;
        private Long deliveryId;
        private Integer maxPeople;
        private String orderTime;
        private String accountNumber;

        public static ChatRoomResponse from(Delivery delivery, String accountNumber) {
            return ChatRoomResponse.builder()
                    .chatRoomId(delivery.getChatRoom().getId())
                    .deliveryId(delivery.getId())
                    .maxPeople(delivery.getMaxPeople())
                    .orderTime(delivery.getOrderTime().toString())   // TODO: 11:30 AM 형식으로 반환
                    .accountNumber(accountNumber)
                    .build();
        }
    }
}
