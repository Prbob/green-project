package com.brogrammers.brogrammers.sevice;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.Random;

@Service
public class MailService {
    private JavaMailSender mailSender;
    private static final String FROM_ADDRESS = ""; // 발송자 이메일

    public MailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void mailSend(HttpSession session, String u_mail) {
        try {
            MailHandler mailHandler = new MailHandler(mailSender);

            Random random = new Random(System.currentTimeMillis());
            int result = 100000 + random.nextInt(900000);

            mailHandler.setTo(u_mail);
            mailHandler.setFrom(FROM_ADDRESS);
            mailHandler.setSubject("인증번호 입니다.");

            String htmlContent = "<p> 인증번호 : " + result + "<p>";
            mailHandler.setText(htmlContent, true);

            mailHandler.send();

            session.setAttribute(u_mail, result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int check_mail(String u_mail) {
        // 이메일 확인 코드 구현
        return 0; // 더 구체적인 내용을 추가해야 합니다.
    }

    public boolean certification(HttpSession session, String u_mail, int inputCode) {
        try {
            int preparedCode = (int) session.getAttribute(u_mail);
            if (preparedCode == inputCode) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            throw e;
        }
    }
}
