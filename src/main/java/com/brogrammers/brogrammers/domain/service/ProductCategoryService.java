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
    } // 카테고리

    public Page<Products> findProductsByCategoryGender(Category category,String gender ,Pageable pageable){
        return productCategoryRepository.findProductsByCategoryAndGender(category,gender,"every",pageable);
    } // 카테고리 성별

    public void deleteByProducts(Products products){productCategoryRepository.deleteProductCategoryByProducts(products);}

    public Page<Products> findProductsByBrandNameCategory(Brand brand, String name, Category category, Pageable pageable){
        return productCategoryRepository.finProductsByBrandAndNameAndCategory(brand,name,category,pageable);
    } // 상품 목록 가져오기, 브랜드/이름/카테고리
    public Page<Products> findProductsByBrandNameCategoryGender(Brand brand, String name, Category category, Pageable pageable,String gender){
        return productCategoryRepository.finProductsByBrandAndNameAndCategoryAndGender(brand,name,category,gender,"every",pageable);
    } // 상품 목록 가져오기, 브랜드/이름/카테고리/성별

    public Page<Products> findProductsByBrandCategory(Brand brand,Category category, Pageable pageable){
        return productCategoryRepository.finProductsByBrandAndCategory(brand,category,pageable);
    } // 브랜드 / 카테고리

    public Page<Products> findProductsByBrandCategoryGender(Brand brand,Category category,String gender, Pageable pageable){
        return productCategoryRepository.finProductsByBrandAndCategoryAndGender(brand,category,gender,"every",pageable);
    } // 브랜드 / 카테고리 / 성별

    public Page<Products> findProductsByNameCategory(String name, Category category, Pageable pageable){
        return productCategoryRepository.findProductsByNameAndCategory(name,category,pageable);
    } // 이름 카테고리

    public Page<Products> findProductsByNameCategoryGender(String name, Category category,String gender, Pageable pageable){
        return productCategoryRepository.findProductsByNameAndCategoryAndGender(name,category,gender,"every",pageable);
    } // 이름 / 카테고리 / 성별
}
