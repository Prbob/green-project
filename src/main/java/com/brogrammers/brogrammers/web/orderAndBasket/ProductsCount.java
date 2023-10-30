package com.brogrammers.brogrammers.web.orderAndBasket;

import com.brogrammers.brogrammers.domain.product.Products;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class ProductsCount {
    private Products product;
    private int quantity;
    @Builder
    public ProductsCount(Products product, int quantity) {
        this.product = product;
        this.quantity = quantity;
    }
}
