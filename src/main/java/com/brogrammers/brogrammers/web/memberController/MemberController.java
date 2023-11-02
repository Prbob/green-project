package com.brogrammers.brogrammers.web.memberController;

import com.brogrammers.brogrammers.domain.member.Address;
import com.brogrammers.brogrammers.domain.member.Member;
import com.brogrammers.brogrammers.domain.order.Basket;
import com.brogrammers.brogrammers.domain.service.BasketService;
import com.brogrammers.brogrammers.domain.service.LoginService;
import com.brogrammers.brogrammers.domain.service.MemberService;
import com.brogrammers.brogrammers.web.FunctionClass;
import com.brogrammers.brogrammers.web.session.SessionConst;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
@Slf4j
public class MemberController {

    private final MemberService memberService;
    private final LoginService loginService;
    private final BasketService basketService;
    private final FunctionClass fun;
    /////////////////////// 회원가입 //////////////////////
    @GetMapping("/member/joinMember")
    public String joinMemberForm(Model model, @RequestParam(value = "email",required = false) String email){
        MemberForm memberForm = MemberForm.builder().email(email).build();
        model.addAttribute("form",memberForm);
        return "member/joinMemberForm";
    }

    @PostMapping("/member/joinMember")
    public String joinMember(@Valid @ModelAttribute("form") MemberForm form,BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return "member/joinMemberForm";
        }
        if(!form.getPwd().equals(form.getPwdChk())){ //notEqualPwd
            bindingResult.rejectValue("pwdChk","error.pwdChk","비밀번호가 일치하지 않습니다.");
            return "member/joinMemberForm";
        }

        Optional<Member> optionalMember = memberService.validataDuplicateMember(form.getEmail());
        // 에러 판단하기 위한 변수?
        if(optionalMember.isPresent()){ // 중복 확인
            bindingResult.reject("duplicateMember","이미 존재하는 회원");
            return "member/joinMemberForm";
        }
        Member member = new Member();
        member = member.saveMember(form.getEmail(),form.getPwd(),form.getName());

        if(form.getPostal_code()!=null &&form.getMiddle_address()!=null&&form.getDetailed_address()!=null){
            Address address = new Address(form.getPostal_code(),form.getMiddle_address(),form.getDetailed_address()); // 주소 입력 메서드
            member.saveAddress(address); // 멤버에 주소 적용
        }


        long id = memberService.join(member); // 이 때 데이터 베이스에 쿼리문이 나감


        Basket basket = Basket.builder().member(member).build();
        basketService.save(basket); // 회원 가입하면서 회원 전용 장바구니 하나 만들기.
        log.info("가입 이메일={}, 아이디={}",member.getEmail(),member.getId());
        return "redirect:/";
    }
    /////////////////////// 회원가입 //////////////////////

    @PostMapping("/customer/customerService") // 고객센터
    public String customerService(){
        return "customer/customerService";
    }

    @GetMapping("/member/login")
    public String login(@ModelAttribute("form") LoginForm form,HttpServletRequest request){
        HttpSession session = request.getSession(false);
        if(session != null){
            Member loginMember = (Member) session.getAttribute(SessionConst.LOGIN_MEMBER);
            if(loginMember!=null){
                return "redirect:/";
            }
        }
        return "member/loginForm";

    }

    @PostMapping("/member/login") //
    public String memberLogin(@Valid @ModelAttribute("form") LoginForm form, BindingResult bindingResult, HttpServletRequest request, Model model){
        if(bindingResult.hasErrors()){
            return "member/loginForm";
        }
        Member loginMember = loginService.login(form.getMemberEmail(),form.getPassword());
        if(loginMember==null){
            bindingResult.reject("loginFail","아이디 또는 비밀번호 오류");
            return "member/loginForm";
        }
        HttpSession session = request.getSession();
        session.setAttribute(SessionConst.LOGIN_MEMBER,loginMember); // 로그인 정보 담기
        session.setAttribute(SessionConst.ACCESSRIGHTS,loginMember.getAccessrigths());
        model.addAttribute("member",loginMember);
        return "redirect:/";
    }

    @PostMapping("/logout")
    public String logout3(HttpServletRequest request){
        HttpSession session = request.getSession(false);
        if(session!=null){
            session.invalidate();
        }
        return "redirect:/";
    }
    @GetMapping("/logout")
    public String logout(HttpServletRequest request){
        HttpSession session = request.getSession(false);
        if(session!=null){
            session.invalidate();
        }
        return "redirect:/";
    }


    // 비밀번호 찾기
    @GetMapping("/member/findPassword")
    public String pwdFind() {
        return "member/findPassword";
    }

    @GetMapping("/member/myPage") // 마이페이지
    public String myPage(HttpServletRequest request,Model model){
        Member member = fun.getMemberDb(request);
        model.addAttribute("member",member);
        return "member/myPage";
    }
    @GetMapping("/member/orderDetail")
    public String orderDetail() {
        return "member/orderDetail";
    }
}

