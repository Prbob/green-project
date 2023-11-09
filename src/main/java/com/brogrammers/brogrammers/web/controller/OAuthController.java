package com.brogrammers.brogrammers.web.controller;

import com.brogrammers.brogrammers.domain.member.Member;
import com.brogrammers.brogrammers.domain.service.MemberService;
import com.brogrammers.brogrammers.sevice.OAuthService;
import com.brogrammers.brogrammers.web.memberController.LoginForm;
import com.brogrammers.brogrammers.web.memberController.MemberController;
import com.brogrammers.brogrammers.web.session.SessionConst;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Map;
import java.util.Optional;

//@RestController
@Controller
@RequestMapping(value="/auth/kakao/*",produces = "text/plain;charset=UTF-8")
public class OAuthController {
    @Autowired
    OAuthService oAuthService;
    @Autowired
    MemberController memberController;
    @Autowired MemberService memberService;
    /* 카카오 콜백 */
//    @ResponseBody
    @GetMapping("/callback")
    public String kakaoCallback(@RequestParam(value="code",required=false) String code, HttpServletRequest request,
                                Model model) throws Exception {

        System.out.println("########" + code);
        String code2 = oAuthService.getKakaoAccessToken(code);
        System.out.println("====================");
        String email = oAuthService.createKakaoUser(code2);
        System.out.println("====================");
        System.out.println("====================");
        System.out.println(email);
        HttpSession session = request.getSession();
        Optional<Member> memberChk = memberService.findByEmail(email);
        if(memberChk.isPresent()){
            session.setAttribute(SessionConst.LOGIN_MEMBER,memberChk.get()); // 로그인 정보 담아주기
            return "alert/kakaologinSuccess";
        }
        model.addAttribute("email",email);
        return "alert/kakaologin";
    }


}
