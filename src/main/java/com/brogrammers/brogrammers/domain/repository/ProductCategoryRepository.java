package com.brogrammers.brogrammers.domain.repository;

import com.brogrammers.brogrammers.domain.product.Category;
import com.brogrammers.brogrammers.domain.product.Products;
import com.brogrammers.brogrammers.domain.product.ProductsCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProductCategoryRepository extends JpaRepository<ProductsCategory,Long> {

    @Query("SELECT pc.products FROM ProductsCategory pc WHERE pc.category = :category")
    Page<Products> findProductsByCategory(@Param("category") Category category, Pageable pageable);

//    void deleteProductCategoryByProducts(@Param("products") Products products);
    void deleteProductCategoryByProducts(Products products);
}
