package com.duktown.domain.withdrawalUser.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import static lombok.AccessLevel.PRIVATE;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@Builder
@AllArgsConstructor(access = PRIVATE)
@NoArgsConstructor(access = PROTECTED)
@Table(name = "withdrawal_user")
public class WithdrawalUser {

    @Id
    @GeneratedValue
    @Column(name = "withdrawal_user_id")
    private Long id;
}
