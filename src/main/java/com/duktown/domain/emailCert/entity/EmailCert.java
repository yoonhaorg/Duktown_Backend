package com.duktown.domain.emailCert.entity;

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
@Table(name = "email_cert")
public class EmailCert {

    @Id
    @GeneratedValue
    @Column(name = "email_cert_id")
    private Long id;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String certCode;

    private Boolean certified;
}
