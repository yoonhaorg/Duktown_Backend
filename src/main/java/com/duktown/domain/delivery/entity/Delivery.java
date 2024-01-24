package com.duktown.domain.delivery.entity;

import com.duktown.domain.BaseTimeEntity;
import com.duktown.domain.chatRoom.entity.ChatRoom;
import com.duktown.domain.comment.entity.Comment;
import com.duktown.domain.user.entity.User;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Size;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.FetchType.*;
import static javax.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.*;

@Entity
@Getter
@Builder
@AllArgsConstructor(access = PRIVATE)
@NoArgsConstructor(access = PROTECTED)
public class Delivery extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "delivery_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToOne(fetch = LAZY)
    @JoinColumn(name = "delivery_id", nullable = false)
    private ChatRoom chatRoom;

    @Column(length = 20, nullable = false)
    private String title;

    @Size(min = 2)
    @Column(nullable = false)
    private Integer maxPeople;

    @Column(nullable = false)
    private LocalDateTime orderTime;

    @Column(nullable = false)
    private String accountNumber;

    @Column(nullable = false, columnDefinition = "longtext")
    private String content;

    @Builder.Default
    @Column(nullable = false)
    private Boolean active = true;

    @Builder.Default
    @OneToMany(fetch = LAZY, mappedBy = "delivery")
    private List<Comment> comments = new ArrayList<>();

    @Builder.Default
    @Column(nullable = false)
    private Boolean deleted = false;

    public void closeDelivery() {
        this.active = false;
    }

    public void updateAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public void delete() {
        this.deleted = true;
    }
}
