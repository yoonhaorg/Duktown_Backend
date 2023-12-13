package com.duktown.domain.delivery.controller;

import com.duktown.domain.delivery.dto.DeliveryDto;
import com.duktown.domain.delivery.service.DeliveryService;
import com.duktown.global.security.service.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/delivery")
public class DeliveryController {
    private final DeliveryService deliveryService;

    @PostMapping()
    public ResponseEntity<Void> createDelivery(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @Valid @RequestBody DeliveryDto.CreateRequest request
    ) {
        deliveryService.createDelivery(customUserDetails.getId(), request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
