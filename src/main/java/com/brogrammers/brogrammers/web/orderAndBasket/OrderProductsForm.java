package com.brogrammers.brogrammers.web.orderAndBasket;

import com.brogrammers.brogrammers.domain.product.Products;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class OrderProductsForm {

    private String name; // member 객체에서 가지고 와야함.
    private String phone; // member 객체에서 가지고 와야함
    private String phone2; // member 객체에서 가지고 와야함
    private String phone3; // member 객체에서 가지고 와야함

    @Builder
    public OrderProductsForm(String name, String phone, String phone2, String phone3, String payment, String postal_code, String middle_address, String detailed_address, List<ProductsCount> productCounts, int totalPrice) {
        this.name = name;
        this.phone = phone;
        this.phone2 = phone2;
        this.phone3 = phone3;
        this.payment = payment;
        this.postal_code = postal_code;
        this.middle_address = middle_address;
        this.detailed_address = detailed_address;
        this.productCounts = productCounts;
        this.totalPrice = totalPrice;
    }

    private String payment; // 결제수단

    private String postal_code; // 배송지
    private String middle_address; // 배송지
    private String detailed_address; // 배송지
    private List<ProductsCount> productCounts; // 상품 몇 개 주문했는지
    private int totalPrice;



    


}
