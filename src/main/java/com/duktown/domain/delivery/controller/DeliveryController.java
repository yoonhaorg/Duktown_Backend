package com.duktown.domain.delivery.controller;

import com.duktown.domain.delivery.dto.DeliveryDto;
import com.duktown.domain.delivery.service.DeliveryService;
import com.duktown.domain.post.dto.PostDto;
import com.duktown.global.security.service.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping
    public ResponseEntity<DeliveryDto.DeliveryListResponse> getDeliveryList(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @RequestParam(value = "sortBy", required = false) Integer sortBy
    ) {
        return ResponseEntity.ok(
                deliveryService.getDeliveryList(customUserDetails.getId(), sortBy)
        );
    }

    @GetMapping("/{deliveryId}")
    public ResponseEntity<DeliveryDto.DeliveryResponse> getDeliveryDetail(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @PathVariable("deliveryId") Long deliveryId
    ) {
        return ResponseEntity.ok(
                deliveryService.getDeliveryDetail(customUserDetails.getId(), deliveryId)
        );
    }

    @PatchMapping("/{deliveryId}/update")
    public ResponseEntity<Void> updateAccountNumber(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @PathVariable("deliveryId") Long deliveryId,
            @Valid @RequestBody DeliveryDto.AccountUpdateRequest request
    ) {
        deliveryService.updateAccountNumber(customUserDetails.getId(), deliveryId, request);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{deliveryId}/close")
    public ResponseEntity<Void> closeDelivery(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @PathVariable("deliveryId") Long deliveryId
    ) {
        deliveryService.closeDelivery(customUserDetails.getId(), deliveryId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{deliveryId}/complete")
    public ResponseEntity<Void> completeDelivery(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @PathVariable("deliveryId") Long deliveryId
    ) {
        deliveryService.completeDelivery(customUserDetails.getId(), deliveryId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{deliveryId}")
    public ResponseEntity<Void> deleteDelivery(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @PathVariable("deliveryId") Long deliveryId
    ) {
        deliveryService.deleteDelivery(customUserDetails.getId(), deliveryId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/search")
    public ResponseEntity<?> searchDelivery(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @RequestParam("keyword") String keyword
    ){
        return ResponseEntity.ok().body(
                deliveryService.searchDeliveryList(customUserDetails.getId(),keyword));
    }
}
