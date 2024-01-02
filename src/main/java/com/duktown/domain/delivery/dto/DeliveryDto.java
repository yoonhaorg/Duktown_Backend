package com.duktown.domain.delivery.dto;

import com.duktown.domain.delivery.entity.Delivery;
import com.duktown.domain.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Future;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class DeliveryDto {

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateRequest {
        @NotBlank(message = "배달팟 제목은 필수 값입니다.")
        private String title;

        @NotNull(message = "모집인원은 필수 값입니다.")
        @Min(value = 1, message = "모집인원의 최소 값은 1 이상이어야 합니다.")
        private int maxPeople;

        @NotNull(message = "주문 예정 시각은 필수 값입니다.")
        @Future(message = "주문 예정 시각은 현재 시각 이후여야 합니다.")
        private LocalDateTime orderTime;

        @NotBlank(message = "송금 받을 계좌는 필수 값입니다.")
        private String accountNumber;

        @NotBlank(message = "내용은 필수 값입니다.")
        private String content;

        public Delivery toEntity(User user, String encryptedAccountNumber) {
            return Delivery.builder()
                    .user(user)
                    .title(title)
                    .maxPeople(maxPeople)
                    .orderTime(orderTime)
                    .accountNumber(encryptedAccountNumber)
                    .content(content)
                    .build();
        }
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AccountUpdateRequest {
        @NotBlank(message = "송금 받을 계좌는 필수 값입니다.")
        private String accountNumber;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    public static class DeliveryResponse {
        private Long userId;
        private Long deliveryId;
        private LocalDateTime createdAt; //TODO: 날짜 표시 수정
        private Integer maxPeople;
        private LocalDateTime orderTime;
        private String content;
        private Integer peopleCount;
        private Integer commentCount;
        private Boolean active;


        public static DeliveryResponse from(Delivery delivery) {
            return DeliveryResponse.builder()
                    .userId(delivery.getUser().getId())
                    .deliveryId(delivery.getId())
                    .createdAt(delivery.getCreatedAt())
                    .maxPeople(delivery.getMaxPeople())
                    .orderTime(delivery.getOrderTime())
                    .content(delivery.getContent())
                    .peopleCount(delivery.getChatRoom().getChatRoomUsers().size())
                    .commentCount(delivery.getComments().size())
                    .active(delivery.getActive())
                    .build();
        }
    }

    @Getter
    @Builder
    @AllArgsConstructor
    public static class DeliveryListResponse {
        private List<DeliveryResponse> content;

        public static DeliveryListResponse from(List<Delivery> deliveries) {
            return DeliveryListResponse.builder()
                    .content(deliveries
                            .stream()
                            .map(DeliveryResponse::from)
                            .collect(Collectors.toList()))
                    .build();
        }
    }
}
