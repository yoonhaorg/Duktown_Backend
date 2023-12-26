package com.duktown.domain.chatRoomUser.controller;

import com.duktown.domain.chatRoomUser.dto.ChatRoomUserDto;
import com.duktown.domain.chatRoomUser.service.ChatRoomUserService;
import com.duktown.global.security.service.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/chatRoomUser")
public class ChatRoomUserController {

    private final ChatRoomUserService chatRoomUserService;

    @PatchMapping("/block")
    public ResponseEntity<Void> blockChatRoomUser(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @Valid @RequestBody ChatRoomUserDto.BlockRequest request
    ) {
        chatRoomUserService.blockChatRoomUser(customUserDetails.getId(), request);
        return ResponseEntity.ok().build();
    }
}
