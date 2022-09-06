package com.example.demo.utils;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class CryptoUtil {
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(16);

    public String encryptPassword(String password) {
        return encoder.encode(password);
    }

    public Boolean validatePassword(String plainPassword, String encryptedPassword) {
        return encoder.matches(plainPassword, encryptedPassword);
    }
}
