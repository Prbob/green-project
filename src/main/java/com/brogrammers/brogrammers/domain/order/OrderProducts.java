package com.brogrammers.brogrammers.domain.order;

import com.brogrammers.brogrammers.domain.product.Products;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class OrderProducts {
    @Builder
    public OrderProducts(int quantity, Products products, Orders orders) {
        this.quantity = quantity;
        this.products = products;
        this.orders = orders;
    }

    @Id @GeneratedValue
    @Column(name="order_Products_id")
    private Long id;

    @Column(name="order_products_quantity")
    private int quantity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "products_id")
    private Products products;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="orders_id")
    private Orders orders;

}
