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
import org.springframework.web.bind.annotation.*;

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
    @Autowired private FunctionClass fun;
    @Autowired private ProductService productService;
    // 배송지 정보를 담아주어야함.
    // 주문자 정보 => 회원 정보 => member정보
    // 상품 정보(orderProducts) => 장바구니에서 어떤 물품들 있는지 조회 => 오더상품으로 맵핑해주어야함
    // 결제수단?
    // 현금 영수증
    @GetMapping("/order/buy") // 장바구니 구매 페이지
    public String orderForm(@RequestParam(name = "product",required = false)Long id,@RequestParam(name = "quantity",required = false)Integer quantity,
                            Model model, HttpServletRequest request,@RequestParam(name = "way",required = false)String way){
        if(fun.getMember(request)==null){return "/alert/orderNologin";}

        Member member = functionClass.getMemberDb(request); // 1. 회원 정보를 불러옴
        Address address = member.getAddress(); // 회원 주소
        if(way!=null && way.equals("one")){
            Products products = productService.getByid(id);
            List<ProductsCount> productOne = new ArrayList<>();
            productOne.add(ProductsCount.builder().product(products).quantity(quantity).build());
            OrderProductsForm form2 = OrderProductsForm.builder() //
                    .name(member.getName()).phone(member.getPhone()+"-"+member.getPhone2()+"-"+member.getPhone3())
                    .postal_code(address.getPostal_code()).middle_address(address.getMiddle_address()).detailed_address(address.getDetailed_address()) // 주소 입력해주기
                    .build();
            form2.setTotalPrice(products.getPrice()*quantity);
            form2.setProductCounts(productOne);
            model.addAttribute("list",productOne); // 내 장바구니 상품들 담아줌.
            model.addAttribute("form",form2);
            return "orderAndBasket/orderForm";
        }
        Basket basket = basketService.findByMemberid(member.getId()).get(); // 1. 장바구니를 불러옴
        List<Products> products = baskProdService.findProductsByBasketId(basket); // 3. 장바구니에 담긴 상품 가져오기
        BasketProducts basketProducts = null;
        List<ProductsCount> productsCounts = new ArrayList<>();
        OrderProductsForm form = OrderProductsForm.builder() //
                .name(member.getName()).phone(member.getPhone()+"-"+member.getPhone2()+"-"+member.getPhone3())
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
    @PostMapping("/order/buy")
    public String order(@ModelAttribute("form") @Valid OrderProductsForm form, BindingResult result, Model model, HttpServletRequest request){
        if(fun.getMember(request)==null){return "/alert/noLogin";}
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
    @GetMapping("/buy")
    public String buyOne(@RequestParam("product")Long id,HttpServletRequest request){
        if(fun.getMember(request)==null){return "/alert/noLogin";}
        return "redirect:/";
    }
}
