package com.brogrammers.brogrammers.web.controller;

import com.brogrammers.brogrammers.sevice.OAuthService;
import com.brogrammers.brogrammers.web.memberController.MemberController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@Controller
@RequestMapping(value="/auth/kakao/*",produces = "text/plain;charset=UTF-8")
public class OAuthController {
    @Autowired
    OAuthService oAuthService;
    @Autowired
    MemberController memberController;
    /* 카카오 콜백 */
    @ResponseBody
    @GetMapping("/callback")
    public void kakaoCallback(@RequestParam(value="code",required=false) String code) throws Exception {
        System.out.println("########" + code);
        String code2 = oAuthService.getKakaoAccessToken(code);
        System.out.println("====================");
        String email = oAuthService.createKakaoUser(code2);
    }


}
