package com.brogrammers.brogrammers.web.memberController;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class PasswordResetService {
    private final JavaMailSender mailSender;
    private static final String FROM_ADDRESS = "your_email@example.com"; // 발송 이메일 주소

    public PasswordResetService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public boolean resetPassword(String email) {
        // 여기에서 DB에서 해당 이메일 주소의 사용자를 찾아서 비밀번호 초기화를 진행합니다.
        // 사용자를 찾지 못하거나 초기화에 실패하면 false를 반환합니다.

        // 임시 비밀번호 생성 후 temporaryPassword에 담아줌
        String temporaryPassword = generateTemporaryPassword();

        // 여기에서 이메일을 보내는 로직을 구현합니다.
        try {
            MailHandler mailHandler = new MailHandler(mailSender);
            mailHandler.setTo(email);
            mailHandler.setFrom(FROM_ADDRESS);
            mailHandler.setSubject("임시 비밀번호 발급");

            String htmlContent = "<p>임시 비밀번호: " + temporaryPassword + "</p>";
            mailHandler.setText(htmlContent, true);
            mailHandler.send();

            // 여기에서 사용자의 비밀번호를 temporaryPassword로 업데이트하고 성공 여부를 반환합니다.
            // 업데이트가 성공하면 true를 반환하고, 그렇지 않으면 false를 반환합니다.
            boolean updateResult = updatePassword(email, temporaryPassword);

            return updateResult;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private String generateTemporaryPassword() {
        // 임시 비밀번호를 생성하는 로직을 구현합니다.
        // 이 예제에서는 랜덤한 6자리 숫자를 생성하고 반환합니다.
        Random random = new Random();
        int temporaryPasswordInt = 100000 + random.nextInt(900000);
        return String.valueOf(temporaryPasswordInt);
    }

    private boolean updatePassword(String email, String temporaryPassword) {
        // 사용자의 비밀번호를 초기화하고 DB에 업데이트하는 로직을 구현합니다.
        // 사용자를 찾아서 해당 비밀번호로 업데이트합니다.

        // 업데이트가 성공하면 true, 실패하면 false를 반환합니다.
        return true; // 성공했다고 가정
    }
}