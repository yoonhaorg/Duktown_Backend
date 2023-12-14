package com.duktown.domain.delivery.service;

import com.duktown.domain.delivery.dto.DeliveryDto;
import com.duktown.domain.delivery.entity.Delivery;
import com.duktown.domain.delivery.entity.DeliveryRepository;
import com.duktown.domain.user.entity.User;
import com.duktown.domain.user.entity.UserRepository;
import com.duktown.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.duktown.global.kisa_SEED.KISA_SEED_CBC;

import java.util.Arrays;

import static com.duktown.global.exception.CustomErrorType.USER_NOT_FOUND;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DeliveryService {
    private final UserRepository userRepository;
    private final DeliveryRepository deliveryRepository;

    @Value("${custom.seed.pbsz.user.key}")
    private String secretKey;

    @Value("${custom.seed.pbsz.iv}")
    private String vectorKey;

    @Transactional
    public void createDelivery(Long userId, DeliveryDto.CreateRequest request) {
        User user = userRepository.findById(userId).orElseThrow(() -> new CustomException(USER_NOT_FOUND));

        byte[] encryptedAccountNumber = KISA_SEED_CBC.SEED_CBC_Encrypt(
                secretKey.getBytes(), vectorKey.getBytes(),
                request.getAccountNumber().getBytes(), 0, request.getAccountNumber().length()
                );
        Delivery delivery = request.toEntity(user, Arrays.toString(encryptedAccountNumber));
        deliveryRepository.save(delivery);
    }
}
