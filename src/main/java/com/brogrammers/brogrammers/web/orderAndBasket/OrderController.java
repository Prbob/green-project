package com.brogrammers.brogrammers.web.orderAndBasket;

import com.brogrammers.brogrammers.domain.member.Address;
import com.brogrammers.brogrammers.domain.member.Member;
import com.brogrammers.brogrammers.domain.order.Basket;
import com.brogrammers.brogrammers.domain.product.BasketProducts;
import com.brogrammers.brogrammers.domain.product.Products;
import com.brogrammers.brogrammers.domain.service.BaskProdService;
import com.brogrammers.brogrammers.domain.service.BasketService;
import com.brogrammers.brogrammers.domain.service.MemberService;
import com.brogrammers.brogrammers.web.FunctionClass;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Controller
public class OrderController {

    @Autowired private FunctionClass functionClass;
    @Autowired private BasketService basketService;
    @Autowired private MemberService memberService;
    @Autowired private BaskProdService baskProdService;

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
                .name(member.getName()).phone(member.getPhone()).phone2(member.getPhone2()).phone3(member.getPhone3()) // 회원 이름, 전화번호 담아주기
                .postal_code(address.getPostal_code()).middle_address(address.getMiddle_address()).detailed_address(address.getDetailed_address()) // 주소 입력해주기
                .build();

        for(Products product : products){
            basketProducts = baskProdService.findBasProByBasAndPro(basket,product).get(); // 장바구니+상품인 테이블 가져옴, 상품 갯수를 가져오기 위함
            ProductsCount productsCount = ProductsCount.builder().product(product).quantity(basketProducts.getQuantity()).build(); // 상품과 갯수를 넣어줌
            productsCounts.add(productsCount);

        }

        form.setProductCounts(productsCounts);

        model.addAttribute("list",products); // 내 장바구니 상품들 담아줌.
        model.addAttribute("form",form);
        System.out.println("오더페이지 컨트롤러 실행");
        return "orderAndBasket/orderFormTest";
    }
    @PostMapping("/order/buyAll")
    public String order(Model model, HttpServletRequest request){
        Member member = functionClass.getMemberDb(request); // 1. 회원 정보를 불러옴
        Basket basket = basketService.findByMemberid(member.getId()).get(); // 1. 장바구니를 불러옴
        List<Products> products = baskProdService.findProductsByBasketId(basket); // 3. 장바구니에 담긴 상품 가져오기
        BasketProducts basketProducts = null;
        for(Products product : products){
            basketProducts = baskProdService.findBasProByBasAndPro(basket,product).get();

        }
//        List<ProductsCount> productsCounts = new ArrayList<>(); // 상품 + 갯수 담아줄 클래스
//        products.forEach(p->ProductsCount.builder().count(1).product(p).build()); // 상품 + 갯수 담아줌.

        Address address = member.getAddress(); // 회원 주소

        OrderProductsForm form = OrderProductsForm.builder()
                .name(member.getName()).phone(member.getPhone())
                .postal_code(address.getPostal_code()).middle_address(address.getMiddle_address()).detailed_address(address.getDetailed_address())
                .build();


        model.addAttribute("form",form);
        System.out.println("오더페이지 컨트롤러 실행");
        return "orderAndBasket/orderFormTest";
    }
}
