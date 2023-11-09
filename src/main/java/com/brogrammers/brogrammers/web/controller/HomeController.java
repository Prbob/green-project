package com.brogrammers.brogrammers.web.controller;

import com.brogrammers.brogrammers.domain.member.Member;
import com.brogrammers.brogrammers.web.FunctionClass;
import com.brogrammers.brogrammers.web.session.SessionConst;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/")
public class HomeController {

    @Autowired FunctionClass fun;

    // HTTP GET 요청을 처리하는 메소드
    @GetMapping(value = {"/home","/",""})
    // HTTP GET 요청을 처리 메소드의 매개변수로 HttpServletRequest와 Model을 받는다
    public String home(HttpServletRequest request, Model model){
        // 헤더 home, style, shop중 home 글자 굵기를 위한
        model.addAttribute("home","home");

        // 새로운 세션을 가져오거나 기존 세션을 가져오는 역할
        // 현재 요청에 대한 세션 객체를 가져온다.
        // 세션이 이미 존재하면 해당 세션을 반환하고, 세션이 존재하지 않으면 null을 반환
        HttpSession session = request.getSession(false);
        if(session==null){
            return "home";
        }

        // 세션에서 사용자 정보를 가져오는 역할
        // web/session/SessionConst.java
        // 세션에서 "LOGIN_MEMBER"라는 속성을 가져와서
        // 이를 Member 객체로 형변환하여 member 변수에 저장
        Member member = (Member)session.getAttribute(SessionConst.LOGIN_MEMBER);
        if(member==null){
            return "home";
        }


        // 로그인 세션이 있을때
        // 데이터를 View(웹 페이지)로 전달하는 역할
        model.addAttribute("member",member);
        // loginHome.html로 전달
        return "home";
    }
    @PostMapping(value = {"/home","/",""})
    // HTTP GET 요청을 처리 메소드의 매개변수로 HttpServletRequest와 Model을 받는다
    public String postHome(HttpServletRequest request, Model model){

        // 새로운 세션을 가져오거나 기존 세션을 가져오는 역할
        // 현재 요청에 대한 세션 객체를 가져온다.
        // 세션이 이미 존재하면 해당 세션을 반환하고, 세션이 존재하지 않으면 null을 반환
        HttpSession session = request.getSession(false);
        if(session==null){
            return "home";
        }

        // 세션에서 사용자 정보를 가져오는 역할
        // web/session/SessionConst.java
        // 세션에서 "LOGIN_MEMBER"라는 속성을 가져와서
        // 이를 Member 객체로 형변환하여 member 변수에 저장
        Member member = (Member)session.getAttribute(SessionConst.LOGIN_MEMBER);
        if(member==null){
            return "home";
        }


        // 로그인 세션이 있을때
        // 데이터를 View(웹 페이지)로 전달하는 역할
        model.addAttribute("member",member);
        // loginHome.html로 전달
        return "home";
    }
    @GetMapping("/manager") //  매니저 페이지로 이동
    public String managerPage(HttpServletRequest request, Model model){
        Member member = fun.getMember(request);
        model.addAttribute("member",member);
        return "managerPage";
    }

}
