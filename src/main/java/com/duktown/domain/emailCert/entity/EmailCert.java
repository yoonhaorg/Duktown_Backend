package com.duktown.domain.emailCert.entity;

import com.duktown.domain.BaseTimeEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import static javax.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PRIVATE;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@Builder
@AllArgsConstructor(access = PRIVATE)
@NoArgsConstructor(access = PROTECTED)
@Table(name = "email_cert")
public class EmailCert extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "email_cert_id")
    private Long id;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String certCode;

    @Column(nullable = false)
    private Boolean certified;

    public void updateCertCode(String certCode) {
        this.certCode = certCode;
    }

    public void updateCertified(Boolean certified) {
        this.certified = certified;
    }
}
