package com.brogrammers.brogrammers.web.orderAndBasket;

import com.brogrammers.brogrammers.domain.member.Address;
import com.brogrammers.brogrammers.domain.member.Member;
import com.brogrammers.brogrammers.domain.order.Basket;
import com.brogrammers.brogrammers.domain.order.OrderProducts;
import com.brogrammers.brogrammers.domain.product.BasketProducts;
import com.brogrammers.brogrammers.domain.product.Products;
import com.brogrammers.brogrammers.domain.service.*;
import com.brogrammers.brogrammers.web.FunctionClass;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Controller
public class OrderController {

    @Autowired private FunctionClass functionClass;
    @Autowired private BasketService basketService;
    @Autowired private MemberService memberService;
    @Autowired private BaskProdService baskProdService;
    @Autowired private OrderService orderService;
    @Autowired private OrderProductsService orderProductsService;
    // 배송지 정보를 담아주어야함.
    // 주문자 정보 => 회원 정보 => member정보
    // 상품 정보(orderProducts) => 장바구니에서 어떤 물품들 있는지 조회 => 오더상품으로 맵핑해주어야함
    // 결제수단?
    // 현금 영수증
    @GetMapping("/order/buyAll")
    public String orderForm(Model model, HttpServletRequest request){
        Member member = functionClass.getMemberDb(request); // 1. 회원 정보를 불러옴
        Basket basket = basketService.findByMemberid(member.getId()).get(); // 1. 장바구니를 불러옴
        List<Products> products = baskProdService.findProductsByBasketId(basket); // 3. 장바구니에 담긴 상품 가져오기
        BasketProducts basketProducts = null;
        Address address = member.getAddress(); // 회원 주소
        List<ProductsCount> productsCounts = new ArrayList<>();
        OrderProductsForm form = OrderProductsForm.builder() //
                .name(member.getName()).phone(member.getPhone()+"-"+member.getPhone2()+"-"+member.getPhone3())
//                .phone2(member.getPhone2()).phone3(member.getPhone3()) // 회원 이름, 전화번호 담아주기
                .postal_code(address.getPostal_code()).middle_address(address.getMiddle_address()).detailed_address(address.getDetailed_address()) // 주소 입력해주기
                .build();
        int totalPrice = 0;
        for(Products product : products){
            basketProducts = baskProdService.findBasProByBasAndPro(basket,product).get(); // 장바구니+상품인 테이블 가져옴, 상품 갯수를 가져오기 위함
            ProductsCount productsCount = ProductsCount.builder().product(product).quantity(basketProducts.getQuantity()).build(); // 상품과 갯수를 넣어줌
            productsCounts.add(productsCount);
            totalPrice += product.getPrice() * basketProducts.getQuantity();

        }

        form.setTotalPrice(totalPrice);
        form.setProductCounts(productsCounts);

        model.addAttribute("list",products); // 내 장바구니 상품들 담아줌.
        model.addAttribute("form",form);
        System.out.println("오더페이지 컨트롤러 실행");
        return "orderAndBasket/orderForm";
    }
    @PostMapping("/order/buyAll")
    public String order(@ModelAttribute("form") @Valid OrderProductsForm form, BindingResult result, Model model, HttpServletRequest request){
        if(result.hasErrors()){
            return "redirect:/order/buyAll";
        }
        List<ProductsCount> productsCounts = new ArrayList<>();
        Member member = functionClass.getMemberDb(request); // 1. 회원 정보를 불러옴
        Basket basket = basketService.findByMemberid(member.getId()).get(); // 1. 장바구니를 불러옴
        List<Products> products = baskProdService.findProductsByBasketId(basket); // 3. 장바구니에 담긴 상품 가져오기
        BasketProducts basketProducts = null;
        for(Products product : products){
            basketProducts = baskProdService.findBasProByBasAndPro(basket,product).get();
            ProductsCount productsCount = ProductsCount.builder().product(product).quantity(basketProducts.getQuantity()).build(); // 상품과 갯수를 넣어줌
            productsCounts.add(productsCount);
        }

        Long orderId = orderService.saveOrderFromOrderController(form, productsCounts, member); // 주문 아이디
        List<OrderProducts> orderProducts = orderProductsService.findOrderproductsByOrders(orderService.findById(orderId).get());


        System.out.println("오더/바이올 포스트 맵핑입니다.");
        System.out.println("이름" + form.getName());
        System.out.println("우편번호"+form.getPostal_code());
        System.out.println("주소"+form.getMiddle_address());
        System.out.println("상세주소"+form.getDetailed_address());
        System.out.println("핸드폰번호"+form.getPhone());
        System.out.println("요청사항"+form.getPlease());
        System.out.println("총 주문 금액" + form.getTotalPrice());
        System.out.println("이름" + form.getName());

        return "orderAndBasket/orderFormTest";
    }
}
