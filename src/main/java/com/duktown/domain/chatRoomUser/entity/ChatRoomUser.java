package com.duktown.domain.chatRoomUser.entity;

import com.duktown.domain.BaseTimeEntity;
import com.duktown.domain.chatRoom.entity.ChatRoom;
import com.duktown.domain.user.entity.User;
import com.duktown.global.type.ChatRoomUserType;
import lombok.*;

import javax.persistence.*;

import static javax.persistence.EnumType.STRING;
import static javax.persistence.FetchType.LAZY;
import static lombok.AccessLevel.*;

@Entity
@Builder
@Getter
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor(access = PRIVATE)
@Table(name = "chat_room_users")
public class ChatRoomUser extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chat_room_user_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "chat_room_id", nullable = false)
    private ChatRoom chatRoom;

    private Integer userNumber;

    @Enumerated(STRING)
    @Column(nullable = false)
    private ChatRoomUserType chatRoomUserType;

    public void changeChatRoomUserType(ChatRoomUserType chatRoomUserType) {
        this.chatRoomUserType = chatRoomUserType;
    }
}
