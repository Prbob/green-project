package com.brogrammers.brogrammers.web.memberController;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class PasswordResetController {
    private final PasswordResetService passwordResetService;

    // PasswordResetService를 주입받는 생성자
    public PasswordResetController(PasswordResetService passwordResetService) {
        this.passwordResetService = passwordResetService;
    }

        // 비밀번호 초기화 페이지를 보여주는 요청을 처리하는 메서드
        @RequestMapping("/findPassword")
        public String showFindPasswordPage() {
            return "findPassword";
        }

    // 비밀번호 초기화를 수행하는 POST 요청을 처리하는 메서드
        @PostMapping("/resetPassword")
        public ModelAndView resetPassword(@RequestParam String email) {
            ModelAndView modelAndView = new ModelAndView();
            // 사용자 이메일 주소를 받아 비밀번호 초기화 서비스를 실행하고 그 결과에 따라 적절한 뷰 페이지로 이동합니다.
            if (passwordResetService.resetPassword(email)) {
            modelAndView.setViewName("/member/passwordResetSuccess"); // 임시 비밀번호 발급 성공 페이지
        } else {
            modelAndView.setViewName("passwordResetError"); // 임시 비밀번호 발급 실패 페이지
        }
            // 최종적으로 ModelAndView를 반환하여 사용자에게 결과 페이지를 보여줍니다.
        return modelAndView;
    }
}