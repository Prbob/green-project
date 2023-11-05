package com.brogrammers.brogrammers.web.orderAndBasket;

import com.brogrammers.brogrammers.domain.member.Member;
import com.brogrammers.brogrammers.domain.order.Basket;
import com.brogrammers.brogrammers.domain.product.BasketProducts;
import com.brogrammers.brogrammers.domain.product.Products;
import com.brogrammers.brogrammers.domain.service.BaskProdService;
import com.brogrammers.brogrammers.domain.service.BasketService;
import com.brogrammers.brogrammers.domain.service.MemberService;
import com.brogrammers.brogrammers.domain.service.ProductService;
import com.brogrammers.brogrammers.web.FunctionClass;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@Slf4j
public class BasketController {
    @Autowired
    private MemberService memberService;
    @Autowired
    private ProductService productService;
    @Autowired
    private BasketService basketService;
    @Autowired
    private BaskProdService baskProdService; // 다대다 중간 다리 테이블
    @Autowired
    private FunctionClass fun;

    //////////////////// 상품 장바구니로 ///////////////////////
    @PostMapping("/order/basket/{productId}")
    public String addBasket(@PathVariable("productId") Long productId, HttpServletRequest request, Model model,
                            @RequestParam("quantity")int quantity, @RequestParam("action")String action,@RequestParam(name = "sizeParam",required = false)Integer sizeParam){
            Products p = productService.getByid(productId);
            Products product;
            if(action.equals("myBasket")){
                product = productService.findProductByNameColorSizeBrand(p.getName(), p.getColor(), p.getSize(), p.getBrand()).get();
            } else{
                product = productService.findProductByNameColorSizeBrand(p.getName(), p.getColor(), sizeParam, p.getBrand()).get();
            }
        if(action.equals("buy")){
            return "redirect:/order/buy?product="+product.getId()+"&quantity="+quantity+"&way=one";
        }
        if(fun.getMember(request)==null){return "/alert/orderNologin";}
        Member member = fun.getMember(request); // 로그인 정보
        Long basketId = member.getBasket().getId(); // 바스켓 아이디 가져오기
        Basket basket = basketService.findById(basketId); // 바스켓 가져오기
        Optional<BasketProducts> optionalBasketProducts = baskProdService.findBasProByBasAndPro(basket,product);
        BasketProducts basketProducts = null;
        if(optionalBasketProducts.isPresent()){
            basketProducts = optionalBasketProducts.get();
            basketProducts.setQuantity(quantity);
            baskProdService.baskProdUpdate(basketProducts); // 수량 수정
        } else{
            basketProducts = BasketProducts.builder()
                    .quantity(quantity)
                    .products(product)
                    .basket(basket)
                    .build();
            baskProdService.save(basketProducts);
        }
        if (action.equals("myBasket")){
            return "redirect:/products/myBasket";
        }
        model.addAttribute("product",product);
        return "redirect:/products/detail/"+productId;
    }
    //////////////////// 상품 장바구니로 ///////////////////////

    @GetMapping("/products/myBasket") // 내 장바구니 이동
    public String myBasket(Model model, HttpServletRequest request,
                           @PageableDefault(page=0,size=5,sort="id",direction = Sort.Direction.DESC) Pageable pageable){
        if(fun.getMember(request)==null){return "/alert/noLogin";}
        Member member = memberService.findById(fun.getMember(request).getId()); // 멤버 가져오기
        Basket basket = basketService.findById(member.getBasket().getId()); // 장바구니 가져오기
        Page<Products> productList = baskProdService.findProductsByBasket(basket,pageable); // 장바구니에 저장된 모든 상품들 가져오기
        List<BasketInProductsForm> list = new ArrayList<>();
        for(Products product : productList){

            BasketProducts basketProducts = baskProdService.findBasProByBasAndPro(basket, product).get();
            list.add(BasketInProductsForm.builder().products(product).quantity(basketProducts.getQuantity()).basProductId(basketProducts.getId()).build());
            // 상품과 수량 담아주기
        }
        if(productList.isEmpty()){
            model.addAttribute("nolist","nolist");

        } else{
            model.addAttribute("nolist","yesList");
        }
        int nowPage = productList.getPageable().getPageNumber() + 1; // 5
        int startPage = Math.max(1,nowPage%5==0?nowPage-4:nowPage/5*5+1);
        int endPage = Math.min(productList.getTotalPages(),startPage+4);
        model.addAttribute("totalPage",productList.getTotalPages());
        model.addAttribute("nowPage",nowPage);
        model.addAttribute("startPage",startPage);
        model.addAttribute("endPage",endPage);
        model.addAttribute("list",list);
        model.addAttribute("basket","basket");
        return "orderAndBasket/myBasket";
    }

    @GetMapping("/basket/delete/{id}")
    public String deleteProductInMyBasket(@PathVariable("id")Long id, HttpServletRequest request){ // 장바구니 삭제 메서드
        baskProdService.delete(id);
        return "redirect:/products/myBasket";
    }


}
