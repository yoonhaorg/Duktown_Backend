package com.duktown.domain.delivery.dto;

import com.duktown.domain.delivery.entity.Delivery;
import com.duktown.domain.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Future;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

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
}
