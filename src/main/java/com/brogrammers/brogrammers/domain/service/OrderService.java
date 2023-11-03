package com.brogrammers.brogrammers.domain.service;

import com.brogrammers.brogrammers.domain.member.Member;
import com.brogrammers.brogrammers.domain.order.Basket;
import com.brogrammers.brogrammers.domain.order.OrderProducts;
import com.brogrammers.brogrammers.domain.order.Orders;
import com.brogrammers.brogrammers.domain.product.Products;
import com.brogrammers.brogrammers.domain.repository.OrdersRepository;
import com.brogrammers.brogrammers.web.orderAndBasket.OrderProductsForm;
import com.brogrammers.brogrammers.web.orderAndBasket.ProductsCount;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderService {

    private final OrdersRepository ordersRepository;
    private final OrderProductsService orderProductsService;
    private final ProductService productService;
    private final BaskProdService baskProdService;
    private final BasketService basketService;
    public Long saveOrderFromOrderController(OrderProductsForm form, List<ProductsCount> productsCount, Member member){
        Orders orders = Orders.builder()
                .totalPrice(form.getTotalPrice())
                .payment(form.getPayment())
                .phone(form.getPhone())
                .member(member)
                .build();
        Orders order = ordersRepository.save(orders);
        Basket basket = basketService.findByMemberid(member.getId()).get();
        OrderProducts orderProducts; // 상품 수량
        for(ProductsCount productsCount1 : productsCount){
            Products products = productService.getByid(productsCount1.getProduct().getId());
            products.setStockQuantity(products.getStockQuantity()-productsCount1.getQuantity()); // 재고 수량 수정
            productService.updateProduct(products);
            orderProducts = OrderProducts.builder()
                    .orders(order)
                    .quantity(productsCount1.getQuantity())
                    .products(productService.getByid(products.getId()))
                    .build();
            orderProductsService.save(orderProducts);
            baskProdService.delete(basket,products);
        }
        return order.getId();
    }

    public Optional<Orders> findById(Long id){
        return ordersRepository.findOrdersById(id);
    }

    public void save(Orders orders){
        ordersRepository.save(orders);
    }
}
