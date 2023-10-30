package com.brogrammers.brogrammers.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SeviceController {

    @GetMapping("/service/style") /* sns */
    public String style(){
        return "service/style";
    }

    @GetMapping("/customer/notice") // 고객센터
    public String notice(){
        return "customer/notice";
    }

    @GetMapping("/customer/questions") // 공지사항
    public String questions(){
        return "customer/questions";
    }

    @GetMapping("/customer/inspectionStandards") // 검수기준
    public String inspectionStandards(){
        return "customer/inspectionStandards";
    }

    @GetMapping("/member/emailCertificationForm") //   이메일 인증 폼 이동
    public String emailCertificationForm(){
        return "member/emailCertificationForm";
    }



}
