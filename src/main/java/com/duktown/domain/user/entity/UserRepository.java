package com.duktown.domain.user.entity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByIdAndLoginId(Long id, String loginId);

    Optional<User> findByEmail(String email);

    Optional<User> findByLoginId(String loginId);
}
