package com.brogrammers.brogrammers.domain.product;

import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor
public class ProductsCategory {
    @Id @GeneratedValue
    @Column(name="products_category_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="products_id")
    private Products products;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="category_id")
    private Category category;

    @Builder
    public ProductsCategory(Products products, Category category) {
        this.products = products;
        this.category = category;
    }
}
