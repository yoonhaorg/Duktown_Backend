package com.duktown.domain.userDelivery.entity;

import com.duktown.domain.delivery.entity.Delivery;
import com.duktown.domain.user.entity.User;
import lombok.*;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.*;

@Entity
@Getter
@Builder
@AllArgsConstructor(access = PRIVATE)
@NoArgsConstructor(access = PROTECTED)
@Table(name = "user_delivery")
public class UserDelivery {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "user_delivery_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "delivery_id", nullable = false)
    private Delivery delivery;
}
