package com.duktown.domain.chatRoom.controller;

import com.duktown.domain.chatRoom.service.ChatRoomService;
import com.duktown.domain.chatRoomUser.dto.ChatRoomUserDto;
import com.duktown.global.security.service.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/chatRoom")
public class ChatRoomController {

    private final ChatRoomService chatRoomService;

    @PostMapping("/invite")
    public ResponseEntity<Void> inviteChatRoomUser(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @Valid @RequestBody ChatRoomUserDto.InviteRequest request
            ) {
        chatRoomService.inviteChatRoomUser(customUserDetails.getId(), request);
        return ResponseEntity.ok().build();
    }
}
