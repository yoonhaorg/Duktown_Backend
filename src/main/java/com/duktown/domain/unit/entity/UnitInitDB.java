package com.duktown.domain.unit.entity;

import com.duktown.global.type.HallName;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;

@Component
@RequiredArgsConstructor
public class UnitInitDB {
    private final InitService initService;

    @PostConstruct
    public void init() {
        initService.createInit();
    }

    @Component
    @Transactional
    @RequiredArgsConstructor
    public static class InitService {
        private final EntityManager em;

        public void createInit() {
            // 기숙사 정보 초기화
            Unit unit1 = Unit.builder()
                    .hallName(HallName.GAON1)
                    .floorNumber(1)
                    .buildingNumber(1)
                    .roomNumber(101)
                    .occupancy(2)
                    .build();

            Unit unit3 = Unit.builder()
                    .hallName(HallName.GAON1)
                    .floorNumber(1)
                    .buildingNumber(1)
                    .roomNumber(102)
                    .occupancy(2)
                    .build();

            Unit unit4 = Unit.builder()
                    .hallName(HallName.GAON1)
                    .floorNumber(1)
                    .buildingNumber(1)
                    .roomNumber(103)
                    .occupancy(2)
                    .build();


            //  엔터티를 저장
            em.persist(unit1);
            em.persist(unit3);
            em.persist(unit4);
        }
    }
}