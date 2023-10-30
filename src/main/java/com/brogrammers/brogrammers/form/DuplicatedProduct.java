package com.brogrammers.brogrammers.form;

import com.brogrammers.brogrammers.domain.product.Brand;
import lombok.Builder;
import lombok.Data;

@Data
public class DuplicatedProduct { // 중복 확인을 위한 폼 ?
    private Brand brand;
    private String productName;
    private String productColor;
    private int size;

    @Builder
    public DuplicatedProduct(Brand brand, String productName, String productColor, int size) {
        this.brand = brand;
        this.productName = productName;
        this.productColor = productColor;
        this.size = size;
    }

}
