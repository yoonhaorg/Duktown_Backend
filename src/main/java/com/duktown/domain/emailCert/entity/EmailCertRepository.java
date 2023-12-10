package com.duktown.domain.emailCert.entity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmailCertRepository extends JpaRepository<EmailCert, Long> {
    Optional<EmailCert> findByEmail(String email);

    Optional<EmailCert> findByCertCode(String certCode);
}
