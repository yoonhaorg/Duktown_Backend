package com.duktown.domain.chat.dto;

import com.duktown.domain.chat.entity.Chat;
import com.duktown.domain.chatRoom.entity.ChatRoom;
import com.duktown.domain.chatRoomUser.entity.ChatRoomUser;
import com.duktown.domain.user.entity.User;
import com.duktown.global.type.ChatType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

public class ChatDto {

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MessageRequest {
        @NotNull(message = "유저 아이디는 필수 값입니다.")
        private Long userId;

        @NotBlank(message = "채팅 타입은 필수 값입니다.")
        private String chatType;

        @NotBlank(message = "메시지는 필수 값입니다.")
        private String message;

        public Chat toEntity(User user, ChatRoom chatRoom, ChatType chatType) {
            return Chat.builder()
                    .user(user)
                    .chatRoom(chatRoom)
                    .content(message)
                    .chatType(chatType)
                    .build();
        }
    }

    @Builder
    @Getter
    @AllArgsConstructor
    public static class MessageResponse {
        private Long chatId;
        private Long userId;
        private Integer userNumber;
        private ChatType chatType;
        private String message;
        private String createdAt;

        public static MessageResponse from(Chat chat, ChatRoomUser chatRoomUser) {
            if (chat.getUser() == null) {
                return MessageResponse.builder()
                        .chatId(chat.getId())
                        .chatType(chat.getChatType())
                        .message(chat.getContent())
                        .createdAt(chat.getCreatedAt().toString())
                        .build();
            }

            return MessageResponse.builder()
                    .chatId(chat.getId())
                    .userId(chat.getUser().getId())
                    .userNumber(chatRoomUser.getUserNumber())
                    .chatType(chat.getChatType())
                    .message(chat.getContent())
                    .createdAt(chat.getCreatedAt().toString())
                    .build();
        }
    }

    @Getter
    public static class ListResponse {
        private final List<MessageResponse> messages;

        public ListResponse(List<MessageResponse> messages) {
            this.messages = messages;
        }
    }
}
