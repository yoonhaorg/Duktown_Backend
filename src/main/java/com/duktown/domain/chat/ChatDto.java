package com.duktown.domain.chat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.socket.WebSocketSession;

import java.util.HashSet;
import java.util.Set;

public class ChatDto {
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ChatMessage {
        public enum MessageType {
            ENTER, TALK
        }

        private MessageType type;
        private String roomId;
        private String sender;
        private String message;

        public void setMessage(String message) {
            this.message = message;
        }
    }

    @Getter
    public static class ChatRoom {
        private String roomId;
        private String name;
        private Set<WebSocketSession> sessions = new HashSet<>();

        @Builder
        public ChatRoom(String roomId, String name) {
            this.roomId = roomId;
            this.name = name;
        }

        public void handleActions(WebSocketSession session, ChatMessage message, ChatService chatService) {
            if (message.getType().equals(ChatMessage.MessageType.ENTER)) {
                sessions.add(session);
                message.setMessage(message.getSender() + "님이 입장하셨습니다.");
            }
            sendMessage(message, chatService);
        }

        public <T> void sendMessage(T message, ChatService chatService) {
            sessions.parallelStream().forEach(session -> chatService.sendMessage(session, message));
        }
    }
}
