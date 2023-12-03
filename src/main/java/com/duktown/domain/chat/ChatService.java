package com.duktown.domain.chat;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatService {
    private final ObjectMapper objectMapper;
    private Map<String, ChatDto.ChatRoom> chatRooms;

    @PostConstruct
    private void init() {
        chatRooms = new LinkedHashMap<>();
    }

    public List<ChatDto.ChatRoom> findAllRoom() {
        return new ArrayList<>(chatRooms.values());
    }

    public ChatDto.ChatRoom findRoomById(String roomId) {
        return chatRooms.get(roomId);
    }

    public ChatDto.ChatRoom createRoom(String name) {
        String randomId = UUID.randomUUID().toString();
        ChatDto.ChatRoom chatRoom = ChatDto.ChatRoom.builder()
                .roomId(randomId)
                .name(name)
                .build();
        chatRooms.put(randomId, chatRoom);
        return chatRoom;
    }

    public <T> void sendMessage(WebSocketSession session, T message) {
        try {
            session.sendMessage(new TextMessage(objectMapper.writeValueAsString(message)));
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }
}
