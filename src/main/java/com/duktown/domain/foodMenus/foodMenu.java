package com.duktown.domain.foodMenus;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity @Builder @Setter
@Getter @AllArgsConstructor @NoArgsConstructor
public class foodMenu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String mealType;
    private String dayOfWeek; // 월요일, 화요일, ...
    private String date;      // 날짜 (11월 27일 등)
    private String menu;
}
