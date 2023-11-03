package com.brogrammers.brogrammers.domain.service;

import com.brogrammers.brogrammers.domain.product.Brand;
import com.brogrammers.brogrammers.domain.product.Category;
import com.brogrammers.brogrammers.domain.product.Products;
import com.brogrammers.brogrammers.domain.product.ProductsCategory;
import com.brogrammers.brogrammers.domain.repository.ProductCategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class ProductCategoryService {
    private final ProductCategoryRepository productCategoryRepository;

    public void save(ProductsCategory productsCategory){
        productCategoryRepository.save(productsCategory);
    }

    public Page<Products> findProductsByCategory1(Category category, Pageable pageable){
        return productCategoryRepository.findProductsByCategory(category,pageable);
    }

    public void deleteByProducts(Products products){productCategoryRepository.deleteProductCategoryByProducts(products);}

    public Page<Products> findProductsByBrandNameCategory(Brand brand, String name, Category category, Pageable pageable){
        return productCategoryRepository.finProductsByBrandAndNameAndCategory(brand,name,category,pageable);

    }

    public Page<Products> findProductsByBrandCategory(Brand brand,Category category, Pageable pageable){
        return productCategoryRepository.finProductsByBrandAndCategory(brand,category,pageable);
    }
    public Page<Products> findProductsByNameCategory(String name, Category category, Pageable pageable){
        return productCategoryRepository.findProductsByNameAndCategory(name,category,pageable);
    }
}
