package com.duktown.domain.chat.dto;

import com.duktown.domain.chat.entity.Chat;
import com.duktown.domain.chatRoom.entity.ChatRoom;
import com.duktown.domain.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Slice;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.stream.Collectors;

public class ChatDto {

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MessageRequest {
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

    @Builder
    @Getter
    @AllArgsConstructor
    public static class MessageResponse {
        private Long userId;
        private String message;
        private String createdAt;

        public static MessageResponse from(Chat chat) {
            if (chat.getUser() == null) {
                return MessageResponse.builder()
                        .userId(null)
                        .message(chat.getContent())
                        .createdAt(chat.getCreatedAt().toString())
                        .build();
            }

            return MessageResponse.builder()
                    .userId(chat.getUser().getId())
                    .message(chat.getContent())
                    .createdAt(chat.getCreatedAt().toString())
                    .build();
        }
    }

    @Builder
    @Getter
    @AllArgsConstructor
    public static class ListResponse {
        private List<MessageResponse> messages;

        public static ListResponse from(Slice<Chat> chats) {
            List<MessageResponse> messages = chats.stream()
                    .map(MessageResponse::from)
                    .collect(Collectors.toList());

            return ListResponse.builder()
                    .messages(messages)
                    .build();
        }
    }
}
