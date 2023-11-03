package com.brogrammers.brogrammers.web.memberController;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class PasswordHashConversion {
    public String hashPassword(String plainPassword) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(plainPassword.getBytes(StandardCharsets.UTF_8));

            // 바이트 배열을 16진수 문자열로 변환
            StringBuilder hexString = new StringBuilder(2 * hash.length);
            for (byte b : hash) {
                hexString.append(String.format("%02x", b));
            }

            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            // 예외 처리
            e.printStackTrace();
            return null;
        }
    }
}
