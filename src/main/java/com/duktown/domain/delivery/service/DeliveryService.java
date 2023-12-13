package com.duktown.domain.delivery.service;

import com.duktown.domain.delivery.dto.DeliveryDto;
import com.duktown.domain.delivery.entity.Delivery;
import com.duktown.domain.delivery.entity.DeliveryRepository;
import com.duktown.domain.user.entity.User;
import com.duktown.domain.user.entity.UserRepository;
import com.duktown.global.exception.CustomException;
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

    @Transactional
    public void createDelivery(Long userId, DeliveryDto.CreateRequest request) {
        User user = userRepository.findById(userId).orElseThrow(() -> new CustomException(USER_NOT_FOUND));

        Delivery delivery = request.toEntity(user);
        deliveryRepository.save(delivery);
    }
}
