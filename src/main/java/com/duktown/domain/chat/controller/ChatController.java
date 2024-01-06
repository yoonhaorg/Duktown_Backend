package com.duktown.domain.chat.controller;

import com.duktown.domain.chat.dto.ChatDto;
import com.duktown.domain.chat.service.ChatService;
import com.duktown.global.security.service.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;
    private final SimpMessagingTemplate template;

    @MessageMapping("/chatRoom/{chatRoomId}")   // 실제로는 /pub/chatRoom/{chatRoomId}로 전송됨
    public void sendMessage(@DestinationVariable("chatRoomId") Long chatRoomId,
                            @Valid @RequestBody ChatDto.MessageRequest message) {
        template.convertAndSend("/sub/chatRoom/" + chatRoomId, chatService.saveChat(chatRoomId, message));
    }

    // 채팅방 채팅 조회
    @GetMapping("/chats/{chatRoomId}")
    public ResponseEntity<ChatDto.ListResponse> getChatMessages(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @PathVariable("chatRoomId") Long chatRoomId,
            @PageableDefault Pageable pageable
    ) {
        return ResponseEntity.ok(chatService.getChatMessages(customUserDetails.getId(), chatRoomId, pageable));
    }
}
