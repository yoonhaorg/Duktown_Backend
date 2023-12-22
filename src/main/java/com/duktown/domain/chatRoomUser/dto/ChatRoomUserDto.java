package com.duktown.domain.chatRoomUser.dto;

import com.duktown.domain.chatRoom.entity.ChatRoom;
import com.duktown.domain.chatRoomUser.entity.ChatRoomUser;
import com.duktown.domain.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

public class ChatRoomUserDto {

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class InviteRequest {
        @NotNull(message = "초대하려는 사용자 아이디는 필수 값입니다.")
        private Long inviteUserId;

        @NotNull(message = "배달팟 아이디는 필수 값입니다.")
        private Long deliveryId;

        public ChatRoomUser toEntity(User user, ChatRoom chatRoom, Integer userNumber) {
            return ChatRoomUser.builder()
                    .user(user)
                    .chatRoom(chatRoom)
                    .userNumber(userNumber)
                    .build();
        }
    }
}
