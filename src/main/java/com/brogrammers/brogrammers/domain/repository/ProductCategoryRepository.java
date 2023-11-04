package com.brogrammers.brogrammers.domain.repository;

import com.brogrammers.brogrammers.domain.product.Brand;
import com.brogrammers.brogrammers.domain.product.Category;
import com.brogrammers.brogrammers.domain.product.Products;
import com.brogrammers.brogrammers.domain.product.ProductsCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProductCategoryRepository extends JpaRepository<ProductsCategory,Long> {

    // 카테고리O
    @Query("SELECT pc.products FROM ProductsCategory pc WHERE pc.category = :category")
    Page<Products> findProductsByCategory(@Param("category") Category category, Pageable pageable);

    // 카테고리O 성별O
    @Query("SELECT pc.products FROM ProductsCategory pc JOIN pc.products p WHERE pc.category = :category And (p.gender=:gender OR p.gender=:every)")
    Page<Products> findProductsByCategoryAndGender(@Param("category") Category category,@Param("gender")String gender,@Param("every") String every, Pageable pageable);


//    void deleteProductCategoryByProducts(@Param("products") Products products);
    void deleteProductCategoryByProducts(Products products);

    // 카테고리O 브랜드O 키워드O
    @Query("SELECT pc.products FROM ProductsCategory pc JOIN pc.products p WHERE p.brand=:brand AND p.name LIKE %:name% AND pc.category =:category")
    Page<Products> finProductsByBrandAndNameAndCategory(@Param("brand") Brand brand,@Param("name") String name, @Param("category") Category category,Pageable pageable);

    // 카테고리O 브랜드O 키워드O 성별O
    @Query("SELECT pc.products FROM ProductsCategory pc JOIN pc.products p WHERE p.brand=:brand AND p.name LIKE %:name% AND pc.category =:category AND (p.gender= :gender OR p.gender=:every)")
    Page<Products> finProductsByBrandAndNameAndCategoryAndGender(@Param("brand") Brand brand,@Param("name") String name, @Param("category") Category category,@Param("gender") String gender,@Param("every")String every,Pageable pageable);

    // 카테고리O 브랜드X 키워드O
    @Query("SELECT pc.products FROM ProductsCategory pc JOIN pc.products p WHERE  p.name LIKE %:name% AND pc.category =:category")
    Page<Products> findProductsByNameAndCategory(@Param("name") String name, @Param("category") Category category,Pageable pageable);

    // 카테고리O 브랜드X 키워드O 성별O
    @Query("SELECT pc.products FROM ProductsCategory pc JOIN pc.products p WHERE  p.name LIKE %:name% AND pc.category =:category AND (p.gender=:gender OR p.gender=:every)")
    Page<Products> findProductsByNameAndCategoryAndGender(@Param("name") String name, @Param("category") Category category,@Param("gender")String gender,@Param("every")String every,Pageable pageable);

    // 카테고리O 브랜드O
    @Query("SELECT pc.products FROM ProductsCategory pc JOIN pc.products p WHERE p.brand=:brand AND pc.category =:category")
    Page<Products> finProductsByBrandAndCategory(@Param("brand") Brand brand, @Param("category") Category category,Pageable pageable);

    // 카테고리O 브랜드O 성별O
    @Query("SELECT pc.products FROM ProductsCategory pc JOIN pc.products p WHERE p.brand=:brand AND pc.category =:category AND (p.gender=:gender OR p.gender=:every)")
    Page<Products> finProductsByBrandAndCategoryAndGender(@Param("brand") Brand brand, @Param("category") Category category,@Param("gender")String gender,@Param("every") String every ,Pageable pageable);



}
