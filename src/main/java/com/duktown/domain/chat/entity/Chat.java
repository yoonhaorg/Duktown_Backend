package com.duktown.domain.chat.entity;

import com.duktown.domain.BaseTimeEntity;
import com.duktown.domain.chatRoom.entity.ChatRoom;
import com.duktown.domain.user.entity.User;
import com.duktown.global.type.ChatType;
import lombok.*;

import javax.persistence.*;

import static javax.persistence.EnumType.STRING;
import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.*;

@Entity
@Builder
@Getter
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor(access = PACKAGE)
public class Chat extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "chat_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "chat_room_id", nullable = false)
    private ChatRoom chatRoom;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Enumerated(STRING)
    @Column(nullable = false)
    private ChatType chatType;

    @Column(nullable = false, columnDefinition = "text")
    private String content;

    public void setContent(String content) {
        this.content = content;
    }
}
