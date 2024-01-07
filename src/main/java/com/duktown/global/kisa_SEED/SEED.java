package com.duktown.global.kisa_SEED;

import org.springframework.beans.factory.annotation.Value;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class SEED {
    @Value("${custom.seed.pbsz.user.key}")
    private String secretKey;

    @Value("${custom.seed.pbsz.iv}")
    private String vectorKey;

    public String encrypt(String rawMessage) {
        byte[] message = rawMessage.getBytes(StandardCharsets.UTF_8);
        byte[] encryptedMessage = KISA_SEED_CBC.SEED_CBC_Encrypt(
                secretKey.getBytes(), vectorKey.getBytes(), message, 0, message.length
        );
        byte[] encodedMessage = Base64.getEncoder().encode(encryptedMessage);

        return new String(encodedMessage, StandardCharsets.UTF_8);
    }

    public String decrypt(String encryptedMessage) {
        byte[] decodedMessage = Base64.getDecoder().decode(encryptedMessage);
        byte[] decryptedMessage = KISA_SEED_CBC.SEED_CBC_Decrypt(
                secretKey.getBytes(), vectorKey.getBytes(), decodedMessage, 0, decodedMessage.length
        );

        return new String(decryptedMessage, StandardCharsets.UTF_8);
    }
}
