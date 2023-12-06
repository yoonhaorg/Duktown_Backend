package com.duktown.domain.unit.entity;

import com.duktown.domain.user.entity.User;
import com.duktown.global.type.HallName;
import com.duktown.global.type.RoleType;
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
    public void init(){
        initService.createInit();
    }

    @Component
    @Transactional
    @RequiredArgsConstructor
    public static class InitService {
        private final EntityManager em;

        public void createInit() {

            User dummyUser = User.builder()
                    .name("김덕우")                    // 사용자 이름
                    .email("kduk@dukcung.ac.kr")       // 사용자 이메일
                    .loginId("20202023")                 // 사용자 로그인 ID
                    .password("1234")         // 암호화된 비밀번호 (실제로는 더 안전한 방법을 사용해야 함)
                    .roleType(RoleType.USER)             // 사용자 역할
                    .refreshToken("initialRefreshToken") // 초기 리프레시 토큰 (필요에 따라 설정)
                    .build();
            em.persist(dummyUser);

            // Unit 엔터티 생성 및 초기화
            Unit unit = Unit.builder()
                    .user(dummyUser)
                    .hallName(HallName.GAON1)
                    .floorNumber(1)
                    .buildingNumber(123)
                    .roomNumber(101)
                    .build();

            // Unit 엔터티를 저장
            em.persist(unit);

        }
    }
}

