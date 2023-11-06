package com.brogrammers.brogrammers.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SeviceController {

    @GetMapping("/service/style") /* sns */
    public String style(){
        return "service/style";
    }

    @GetMapping("/customer/notice") // 고객센터
    public String notice(Model model){

        model.addAttribute("notice","notice");
        return "customer/notice";
    }

    @GetMapping("/customer/questions") // 자주 묻는 질문
    public String questions(Model model){
        model.addAttribute("questions","questions");
        return "customer/questions";
    }

    @GetMapping("/customer/inspectionStandards") // 검수기준
    public String inspectionStandards(Model model){
        model.addAttribute("inspectionStandards","inspectionStandards");
        return "customer/inspectionStandards";
    }

    @GetMapping("/member/emailCertificationForm") //   이메일 인증 폼 이동
    public String emailCertificationForm(){
        return "member/emailCertificationForm";
    }



}
