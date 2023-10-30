package com.brogrammers.brogrammers.domain.product;

import com.brogrammers.brogrammers.domain.order.Basket;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class BasketProducts { // 상품과 장바구니 연결
    @Id @GeneratedValue
    @Column(name="basket_products_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name="basket_id")
    private Basket basket;

    @Column(name="basket_quantity") // 상품 수량
    private int quantity;
    public void setQuantity(int quantity){
        this.quantity = quantity;
    }
    @Builder
    public BasketProducts(Basket basket, Products products, int quantity) {
        this.quantity = quantity;
        this.basket = basket;
        this.products = products;
    }
    @ManyToOne
    @JoinColumn(name="products_id")
    private Products products;
}
