package com.duktown.domain.sleepoverApply.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;

@Embeddable
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Address {
    private String zipcode; // 우편번호
    private String streetAddress;// 지번 주소
    private String detailAddress;// 상세 주소
}
