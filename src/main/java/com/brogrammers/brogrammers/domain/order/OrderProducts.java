package com.brogrammers.brogrammers.domain.order;

import com.brogrammers.brogrammers.domain.product.Products;
import lombok.Getter;

import javax.persistence.*;

@Entity
@Getter
public class OrderProducts {

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
