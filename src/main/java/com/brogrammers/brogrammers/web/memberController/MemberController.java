package com.brogrammers.brogrammers.web.memberController;

import com.brogrammers.brogrammers.domain.member.Address;
import com.brogrammers.brogrammers.domain.member.Member;
import com.brogrammers.brogrammers.domain.order.Basket;
import com.brogrammers.brogrammers.domain.order.OrderProducts;
import com.brogrammers.brogrammers.domain.order.Orders;
import com.brogrammers.brogrammers.domain.product.Products;
import com.brogrammers.brogrammers.domain.service.*;
import com.brogrammers.brogrammers.web.FunctionClass;
import com.brogrammers.brogrammers.web.orderAndBasket.ProductsCount;
import com.brogrammers.brogrammers.web.session.SessionConst;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
@Slf4j
public class MemberController {

    private final MemberService memberService;
    private final LoginService loginService;
    private final BasketService basketService;
    private final FunctionClass fun;
    private final OrderService orderService;
    private final OrderProductsService orderProductsService;
//    시큐리티
//    PasswordHashConversion passwordConverter = new PasswordHashConversion();
    /////////////////////// 회원가입 //////////////////////
    @GetMapping("/member/joinMember")
    public String joinMemberForm(Model model, @RequestParam(value = "email",required = false) String email,HttpServletRequest request){
        if(fun.getMember(request)!=null){return "redirect:/";}
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

        // 비밀번호 해싱
//        String hashedPassword = passwordConverter.hashPassword(form.getPwd());

        Member member = new Member();

//        member = member.saveMember(form.getEmail(),hashedPassword,form.getName());
        member = member.saveMember(form.getEmail(),form.getPwd(),form.getName(),form.getPwd());


        if(form.getPostal_code()!=null &&form.getMiddle_address()!=null&&form.getDetailed_address()!=null){
            Address address = new Address(form.getPostal_code(),form.getMiddle_address(),form.getDetailed_address()); // 주소 입력 메서드
            member.saveAddress(address); // 멤버에 주소 적용
        }


        long id = memberService.join(member); // 이 때 데이터 베이스에 쿼리문이 나감


        Basket basket = Basket.builder().member(member).build();
        basketService.save(basket); // 회원 가입하면서 회원 전용 장바구니 하나 만들기.
        log.info("가입 이메일={}, 아이디={}",member.getEmail(),member.getId());
        return "/member/joinComplete";
    }
    /////////////////////// 회원가입 //////////////////////


    @GetMapping("/member/joinComplete") // 회원가입 성공 페이지
    public String joinComplete() {
        return "/member/joinComplete";
    }



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

        // 비밀번호 해싱
//        String hashedPassword = passwordConverter.hashPassword(form.getPassword());
//        Member loginMember = loginService.login(form.getMemberEmail(),hashedPassword);

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

    @GetMapping("/member/myPage") // 마이페이지 / 구매내역 페이지
    public String myPage(HttpServletRequest request, Model model,
                         @PageableDefault(page=0,size=5,sort="id",direction = Sort.Direction.DESC)Pageable pageable){
        if(fun.getMember(request)==null){return "/alert/noLogin";}
        Member member = fun.getMemberDb(request);
        /* 로그인 정보에 맞는 오더들 다 불러옴 */
        Page<Orders> orders = orderService.findOrdersByMember(member, pageable);
        List<OrderedForm> list = new ArrayList<>();
        OrderedForm form;
        if(!orders.isEmpty()){
            for(Orders oneOrder:orders.getContent()){
                form = new OrderedForm();
                form.setOrders(oneOrder);
                String imgName = orderProductsService.findOrderproductsByOrders(oneOrder).get(0).getProducts().getImgName();
                form.setFileName(imgName);
                list.add(form);
            }
        } else{
            model.addAttribute("nolist","nolist");
        }
        int nowPage = orders.getPageable().getPageNumber() + 1; // 5
        int startPage = Math.max(1,nowPage%5==0?nowPage-4:nowPage/5*5+1);
        int endPage = Math.min(orders.getTotalPages(),startPage+4);
        model.addAttribute("totalPage",orders.getTotalPages());
        model.addAttribute("nowPage",nowPage);
        model.addAttribute("startPage",startPage);
        model.addAttribute("endPage",endPage);
        model.addAttribute("list",list);
        model.addAttribute("member",member);
        model.addAttribute("myPage","myPage");
        return "member/myPage";
    }
    @GetMapping("/member/orderDetail")
    public String orderDetail(HttpServletRequest request, Model model,Long orderId) {
        if(fun.getMember(request)==null){return "/alert/noLogin";}
        Member member = fun.getMemberDb(request);
        Orders order = orderService.findById(orderId).get();  // 오더
        List<OrderProducts> orderproductsByOrders = orderProductsService.findOrderproductsByOrders(order);  // 오더로 주문 아이템 내역 가져오기
        List<ProductsCount> productsCountList = new ArrayList<>();
        ProductsCount productsCount;
        for(OrderProducts orderProducts : orderproductsByOrders){
            int quantity = orderProducts.getQuantity();
            Products product = orderProducts.getProducts();
            productsCount = ProductsCount.builder().product(product).quantity(quantity).build();
            productsCountList.add(productsCount);
        }
        if (productsCountList.isEmpty()){
            model.addAttribute("nulllist","nulllist");
        }
        model.addAttribute("order",order);
        model.addAttribute("list",productsCountList);
        model.addAttribute("member",member);
        return "member/orderDetail";
    }

    @GetMapping("/member/myInformationSecurity")
    public String myInformationSecurityForm(HttpServletRequest request,Model model,InfoUpdatPwdChk form){
        if(fun.getMember(request)==null ){return "/alert/noLogin";}
        model.addAttribute("form",form);
        model.addAttribute("myinfo","myinfo");
        return "/member/myInformationSecurity";
    }
    @PostMapping("/member/myInformationSecurity")
    public String myInformationSecurity(HttpServletRequest request,Model model, @Valid @ModelAttribute("form") InfoUpdatPwdChk form,BindingResult result){
        Member member = fun.getMemberDb(request);
        if (result.hasErrors()){return "/member/myInformationSecurity";}
        if(!member.getPwd().equals(form.getPwd())){
            result.rejectValue("pwd","error.pwd","비밀번호가 일치하지 않습니다.");
            return "/member/myInformationSecurity";
        }
        HttpSession session = request.getSession();
        session.setAttribute(SessionConst.MYINFO,"myInfo");
        return "redirect:/member/updateMyInformation";
    }

    @GetMapping("/member/updateMyInformation")
    public String updateMyInformationForm(HttpServletRequest request,Model model){
        if(fun.getMember(request)==null || !fun.myInfo(request)){return "/alert/noLogin";}
        model.addAttribute("myinfo","myinfo");
        Member member = fun.getMemberDb(request);
        MemberForm form = MemberForm.builder()
                .name(member.getName())
                .email(member.getEmail())
                .phone_number(member.getPhone())
                .postal_code(member.getAddress().getPostal_code())
                .middle_address(member.getAddress().getMiddle_address())
                .detailed_address(member.getAddress().getDetailed_address())
                .grade(member.getGrade())
                .build();
        model.addAttribute("form",form);
        return "/member/updateMyInformation";
    }
    @PostMapping("/member/updateMyInformation")
    public String updateMyInformation(@Valid @ModelAttribute("form") MemberForm form,BindingResult result, HttpServletRequest request,Model model){
        if(fun.getMember(request)==null){return "/alert/noLogin";}
        if(result.hasErrors()){
            return "/member/updateMyInformation";
        }

        String pwd = form.getPwd();
        String pwdChk = form.getPwdChk();
        String phone = form.getPhone_number();
        String detailedAddress = form.getDetailed_address();
        String middleAddress = form.getMiddle_address();
        String postalCode = form.getPostal_code();
        if(!pwd.equals(pwdChk)){ //notEqualPwd
            result.rejectValue("pwdChk","error.pwdChk","비밀번호가 일치하지 않습니다.");
            return "/member/updateMyInformation";
        }
        Member member = fun.getMemberDb(request);
        member.updatPwd(form.getPwd()); // 비밀번호 변경
        member.updatPhone(form.getPhone_number()); // 핸드폰 번호 변경
        if(!(form.getPostal_code().isEmpty() && form.getMiddle_address().isEmpty() && form.getDetailed_address().isEmpty())){
            member.saveAddress(Address.builder().detailed_address(detailedAddress).middle_address(middleAddress).postal_code(postalCode).build()); // 주소 업뎃
        }
        memberService.updateMember(member);
        fun.logout(request);
        model.addAttribute("form",form);
        return "/alert/updatMyInformation";
    }

    @GetMapping("/my/withdrawal")
    public String withdrawal(HttpServletRequest request){
        Member member = fun.getMemberDb(request);
        member.updatLeave("bye");
        memberService.updateMember(member);
        fun.logout(request);
        return "alert/bye";
    }
}

