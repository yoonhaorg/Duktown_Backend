package com.duktown.global.exception;

import org.springframework.messaging.Message;
import org.springframework.messaging.MessageDeliveryException;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.StompSubProtocolErrorHandler;

import java.nio.charset.StandardCharsets;

@Component
public class ChatErrorHandler extends StompSubProtocolErrorHandler {
    public ChatErrorHandler() {
        super();
    }

    @Override
    public Message<byte[]> handleClientMessageProcessingError(Message<byte[]> clientMessage, Throwable ex) {
        Throwable convertedEx = convertException(ex);
        if (convertedEx instanceof CustomException) {
            return prepareErrorMessage((CustomException) convertedEx);
        }

        return super.handleClientMessageProcessingError(clientMessage, ex);
    }

    private Throwable convertException(final Throwable exception) {
        if (exception instanceof MessageDeliveryException) {
            return exception.getCause();
        }
        return exception;
    }

    private Message<byte[]> prepareErrorMessage(CustomException customException) {
        final StompHeaderAccessor stompHeaderAccessor = StompHeaderAccessor.create(StompCommand.ERROR);
        stompHeaderAccessor.setNativeHeader("code", String.valueOf(customException.getErrorType().getCode()));
        stompHeaderAccessor.setMessage(String.valueOf(customException.getErrorType().getErrorMessage()));
        stompHeaderAccessor.setLeaveMutable(true);

        return MessageBuilder.createMessage("error".getBytes(StandardCharsets.UTF_8), stompHeaderAccessor.getMessageHeaders());
    }
}
