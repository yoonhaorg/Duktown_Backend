package com.duktown.domain.delivery.entity;

import com.duktown.domain.user.entity.User;
import lombok.*;

import javax.persistence.*;

import java.time.LocalDateTime;

import static javax.persistence.FetchType.*;
import static javax.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.*;

@Entity
@Getter
@Builder
@AllArgsConstructor(access = PRIVATE)
@NoArgsConstructor(access = PROTECTED)
public class Delivery {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "delivery_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(length = 20, nullable = false)
    private String title;

    @Column(nullable = false)
    private LocalDateTime orderTime;

    @Column(nullable = false)
    private Integer maxPeople;

    @Column(nullable = false, columnDefinition = "longtext")
    private String content;

    @Builder.Default
    @Column(nullable = false)
    private Boolean active = true;

    public void closeDelivery() {
        this.active = false;
    }

    public void reOpenDelivery() {
        this.active = true;
    }

    public void update(String title, String content, LocalDateTime orderTime, Integer maxPeople) {
        this.title = title;
        this.content = content;
        this.orderTime = orderTime;
        this.maxPeople = maxPeople;
    }
}
