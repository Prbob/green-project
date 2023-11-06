package com.brogrammers.brogrammers.web.orderAndBasket;

import com.brogrammers.brogrammers.domain.member.Address;
import com.brogrammers.brogrammers.domain.member.Member;
import com.brogrammers.brogrammers.domain.order.Basket;
import com.brogrammers.brogrammers.domain.order.Delivery;
import com.brogrammers.brogrammers.domain.order.OrderProducts;
import com.brogrammers.brogrammers.domain.order.Orders;
import com.brogrammers.brogrammers.domain.product.BasketProducts;
import com.brogrammers.brogrammers.domain.product.Products;
import com.brogrammers.brogrammers.domain.service.*;
import com.brogrammers.brogrammers.web.FunctionClass;
import com.brogrammers.brogrammers.web.session.SessionConst;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.*;

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
    @Autowired private DeliveryService deliveryService;

    // 배송지 정보를 담아주어야함.
    // 주문자 정보 => 회원 정보 => member정보
    // 상품 정보(orderProducts) => 장바구니에서 어떤 물품들 있는지 조회 => 오더상품으로 맵핑해주어야함
    // 결제수단?
    // 현금 영수증
    @GetMapping("/order/buy") // 장바구니 구매 페이지

    public String orderForm(@RequestParam(name = "product",required = false)Long id,@RequestParam(name = "quantity",required = false)Integer quantity,
                            Model model, HttpServletRequest request,@RequestParam(name = "way",required = false)String way){
        if(fun.getMember(request)==null){return "/alert/orderNologin";}
        HttpSession session1 = request.getSession();

        Member member = functionClass.getMemberDb(request); // 1. 회원 정보를 불러옴
        Address address = member.getAddress(); // 회원 주소
        if (way != null && way.equals("one")) {
            Products products = productService.getByid(id);
            List<ProductsCount> productOne = new ArrayList<>();
            productOne.add(ProductsCount.builder().product(products).quantity(quantity).build());
            OrderProductsForm form2 = OrderProductsForm.builder() //

                    .name(member.getName()).phone(member.getPhone())

                    .postal_code(address.getPostal_code()).middle_address(address.getMiddle_address()).detailed_address(address.getDetailed_address()) // 주소 입력해주기
                    .build();
            form2.setTotalPrice(products.getPrice() * quantity);
            form2.setProductCounts(productOne);

            session1.setAttribute(SessionConst.PRODUCTSCOUNT,productOne);
            model.addAttribute("list",productOne); // 내 장바구니 상품들 담아줌.
            model.addAttribute("form",form2);

            return "orderAndBasket/orderForm";
        }
        Basket basket = basketService.findByMemberid(member.getId()).get(); // 1. 장바구니를 불러옴
        List<Products> products = baskProdService.findProductsByBasketId(basket); // 3. 장바구니에 담긴 상품 가져오기
        BasketProducts basketProducts = null;
        List<ProductsCount> productsCounts = new ArrayList<>();
        OrderProductsForm form = OrderProductsForm.builder() //

                .name(member.getName()).phone(member.getPhone())

                .postal_code(address.getPostal_code()).middle_address(address.getMiddle_address()).detailed_address(address.getDetailed_address()) // 주소 입력해주기
                .build();
        int totalPrice = 0;
        for (Products product : products) {
            basketProducts = baskProdService.findBasProByBasAndPro(basket, product).get(); // 장바구니+상품인 테이블 가져옴, 상품 갯수를 가져오기 위함
            ProductsCount productsCount = ProductsCount.builder().product(product).quantity(basketProducts.getQuantity()).build(); // 상품과 갯수를 넣어줌
            productsCounts.add(productsCount);
            totalPrice += product.getPrice() * basketProducts.getQuantity();

        }


        form.setTotalPrice(totalPrice);
        form.setProductCounts(productsCounts);
        HttpSession session = request.getSession();
        session.setAttribute(SessionConst.PRODUCTSCOUNT,productsCounts);

        model.addAttribute("list", products); // 내 장바구니 상품들 담아줌.
        model.addAttribute("form", form);
        System.out.println("오더페이지 컨트롤러 실행");
        return "orderAndBasket/orderForm";
    }


    @PostMapping("/kakaopay")
    public ResponseEntity<Map<String, String>> buyOne(HttpServletRequest request, Model model){
        Map<String, String> response = new HashMap<>();

        Member member = functionClass.getMemberDb(request); // 어떤 회원의 오더인지 구분하기 위함. 오더 테이블에 넣어줄 예정


        // 배송 상태 저장을 해주어야함.
        Delivery delivery = new Delivery();
        Address address = Address.builder() // 오더 객체에 넣을 주소객체
                .detailed_address(request.getParameter("detailed_address"))
                .middle_address(request.getParameter("middle_address"))
                .postal_code(request.getParameter("postal_code"))
                .build();
        delivery.setAddress(address);
        deliveryService.save(delivery); // 딜리버리 테이블에 데이터 삽입, 오더에 넣어줄 예정

        Orders orders = Orders.builder()
                .payment(request.getParameter("payment"))
                .member(member)
                .phone(request.getParameter("phone"))
                .name(request.getParameter("name"))
                .imp_uid(request.getParameter("imp_uid"))
                .totalPrice(Integer.parseInt(request.getParameter("totalPrice")))
                .delivery(delivery)
                .please(request.getParameter("please"))
                .build();
        orderService.save(orders); // 오더 저장 완료
        member.updatTotalOrderPrice(Integer.parseInt(request.getParameter("totalPrice")));
        memberService.updateMember(member);

        HttpSession session = request.getSession();
        Object attribute = session.getAttribute(SessionConst.PRODUCTSCOUNT); // 어떤 상품들 불러오려고 하는 지

        List<ProductsCount> productsCounts;

        if(attribute != null){
            productsCounts = (List<ProductsCount>) attribute;
            for(ProductsCount productsCount: productsCounts){
                Products p = productService.getByid(productsCount.getProduct().getId());
                p.setStockQuantity(p.getStockQuantity()-productsCount.getQuantity());
                productService.updateProduct(p); // 재고 수량 - 주문 수량
                OrderProducts orderProducts = new OrderProducts(productsCount.getQuantity(),p,orders); // 주문 수량, 어떤 상품인지, 어떤 주문에서
                orderProductsService.save(orderProducts);
            }
        }


        response.put("redirectUrl", "/orderAndBasket/orderSuccess?orderId="+orders.getId()); // 리다이렉션할 URL을 설정, 오더 페이지로 설정해야함.

        return ResponseEntity.ok(response);

    }

    @GetMapping("/orderAndBasket/orderSuccess") // 결제 성공 시 이동할 페이지
    public String orderSuccess(@RequestParam("orderId") Long orderId, HttpServletRequest request,Model model){
        if(fun.getMember(request)==null){return "/alert/noLogin";}
        Member memberDb = fun.getMemberDb(request);
        Basket basket = basketService.findByMemberid(memberDb.getId()).get();
        Orders order = orderService.findById(orderId).get();
        Delivery delivery = order.getDelivery();
        Products products;
        List<OrderProducts> list = orderProductsService.findOrderproductsByOrders(order);
        for(OrderProducts orderProducts : list){
            products = orderProducts.getProducts();
            Optional<BasketProducts> basProByBasAndPro = baskProdService.findBasProByBasAndPro(basket, products);
            basProByBasAndPro.ifPresent(basketProducts -> baskProdService.delete(basketProducts.getId()));
        }

        model.addAttribute("list",list);
        model.addAttribute("order",order);
        model.addAttribute("delivery",delivery);
        return "/orderAndBasket/orderSuccess";
    }


}
