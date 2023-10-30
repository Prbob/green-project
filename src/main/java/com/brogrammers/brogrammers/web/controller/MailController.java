package com.brogrammers.brogrammers.web.controller;

import com.brogrammers.brogrammers.sevice.MailService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;


@Controller
@AllArgsConstructor
public class MailController {
    private final MailService mailService;

    @GetMapping("/mail")
    public String dispMail() {
        return "mail";
    }

    @ResponseBody
    @PostMapping("/Checkmail")
    public void execMail(HttpServletRequest request, String u_mail) {
        HttpSession session = request.getSession(); // 세션얻어옴
        mailService.mailSend(session, u_mail); // 메일보내기
    }


    @PostMapping("/certification.do")
    @ResponseBody
    private boolean certification(HttpServletRequest request, String u_mail, String inputCode){
        HttpSession session = request.getSession(); // 세션으로 넘긴값들을 받아옴

        boolean result = mailService.certification(session, u_mail, Integer.parseInt(inputCode));

        return result;
    }
}
