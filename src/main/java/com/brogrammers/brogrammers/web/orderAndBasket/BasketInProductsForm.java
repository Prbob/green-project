package com.brogrammers.brogrammers.web.orderAndBasket;

import com.brogrammers.brogrammers.domain.product.Products;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class BasketInProductsForm { // 바구니에 담긴 상품 + 갯수

    private int quantity;
    private Products products;
    private Long basProductId;

    @Builder
    public BasketInProductsForm(int quantity, Products products, Long basProductId) {
        this.quantity = quantity;
        this.products = products;
        this.basProductId = basProductId;
    }

}
