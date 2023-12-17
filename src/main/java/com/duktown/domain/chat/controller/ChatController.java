package com.duktown.domain.chat.controller;

import com.duktown.domain.chat.dto.ChatDto;
import com.duktown.domain.chat.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;
    private final SimpMessagingTemplate template;

    @MessageMapping("/chatRoom/{chatRoomId}")   // 실제로는 /pub/chatRoom/{chatRoomId}로 전송됨
    public void sendMessage(@DestinationVariable("chatRoomId") Long chatRoomId,
                            @Valid @RequestBody ChatDto.Message message) {
        chatService.saveChat(chatRoomId, message);
        template.convertAndSend("/sub/chatRoom/" + chatRoomId, message);
    }
}
