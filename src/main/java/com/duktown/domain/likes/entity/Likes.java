package com.duktown.domain.likes.entity;

import com.duktown.domain.daily.entity.Daily;
import com.duktown.domain.delivery.entity.Delivery;
import com.duktown.domain.market.entity.Market;
import com.duktown.domain.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.yaml.snakeyaml.error.Mark;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;
import static lombok.AccessLevel.PRIVATE;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@Builder
@AllArgsConstructor(access = PRIVATE)
@NoArgsConstructor(access = PROTECTED)
public class Likes {
    @Id
    @GeneratedValue
    @Column(name = "likes_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "delivery_id", nullable = false)
    private Delivery delivery;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "daily_id", nullable = false)
    private Daily daily;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "market_id", nullable = false)
    private Market market;
}
