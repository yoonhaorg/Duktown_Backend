package com.duktown.domain.user.entity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByIdAndLoginId(Long id, String loginId);

    Optional<User> findByEmail(String email);

    Optional<User> findByLoginId(String loginId);


    //유닛용
    // 유닛 ID로 해당 유닛에 속한 유저 조회
    List<User> findUsersByUnit_Id(Long unitId);
}
