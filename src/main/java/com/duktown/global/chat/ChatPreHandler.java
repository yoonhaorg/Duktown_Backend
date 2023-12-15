package com.duktown.global.chat;

import com.duktown.global.exception.CustomErrorType;
import com.duktown.global.exception.CustomException;
import com.duktown.global.security.provider.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;

@Configuration
@RequiredArgsConstructor
public class ChatPreHandler implements ChannelInterceptor {
    private final JwtTokenProvider jwtTokenProvider;
    public static final String TOKEN_HEADER_PREFIX = "Bearer ";

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(message);
        StompCommand stompCommand = headerAccessor.getCommand();
        if (!stompCommand.equals(StompCommand.CONNECT)) {
            return message;
        }
        String authorization = String.valueOf(headerAccessor.getNativeHeader("Authorization"));
        if (authorization == null || !authorization.startsWith(TOKEN_HEADER_PREFIX)) {
            throw new CustomException(CustomErrorType.INVALID_TOKEN);
        }

        String token = authorization.substring(8, authorization.length() - 1);
        jwtTokenProvider.validateToken(token);

        return message;
    }
}
