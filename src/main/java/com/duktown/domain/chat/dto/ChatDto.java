package com.duktown.domain.chat.dto;

import com.duktown.domain.chat.entity.Chat;
import com.duktown.domain.chatRoom.entity.ChatRoom;
import com.duktown.domain.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

public class ChatDto {

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Message {
        @NotNull(message = "유저 아이디는 필수 값입니다.")
        private Long userId;

        @NotBlank(message = "메시지는 필수 값입니다.")
        private String message;

        public Chat toEntity(User user, ChatRoom chatRoom) {
            return Chat.builder()
                    .user(user)
                    .chatRoom(chatRoom)
                    .content(message)
                    .build();
        }
    }
}
