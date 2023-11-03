package com.duktown.domain.dormCert.entity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DormCertRepository extends JpaRepository<DormCert, Long> {
}
