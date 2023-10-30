package com.brogrammers.brogrammers.web.orderAndBasket;

import com.brogrammers.brogrammers.domain.member.Member;
import com.brogrammers.brogrammers.domain.product.Products;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class BasketForm {

    private Member member;
    private Products products;
    private int quantity;

    @Builder
    public BasketForm(Member member, Products products, int quantity) {
        this.member = member;
        this.products = products;
        this.quantity = quantity;
    }
}
