package com.duktown;

import org.jasypt.encryption.StringEncryptor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

//@SpringBootTest
//@ExtendWith(SpringExtension.class)
public class JasyptTest {
//    @Autowired
//    @Qualifier(value = "jasyptEncryptorAES")
//    private StringEncryptor encryptor;
//
//    @Value("${custom.jasypt.datasource.url}")
//    private String url;
//    @Value("${custom.jasypt.datasource.username}")
//    private String username;
//    @Value("${custom.jasypt.datasource.password}")
//    private String password;

//    @Test()
//    void jsaypt() {
//        System.out.println(jasyptEncoding(url));
//        System.out.println(jasyptEncoding(username));
//        System.out.println(jasyptEncoding(password));
//    }

//    public String jasyptEncoding(String value) {
//        return encryptor.encrypt(value);
//    }
}
