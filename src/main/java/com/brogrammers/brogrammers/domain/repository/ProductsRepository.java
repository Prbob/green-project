package com.brogrammers.brogrammers.domain.repository;

import com.brogrammers.brogrammers.domain.member.Member;
import com.brogrammers.brogrammers.domain.product.Brand;
import com.brogrammers.brogrammers.domain.product.Products;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProductsRepository extends JpaRepository<Products,Long> {
    Products findProductById(Long id);


    Page<Products> findByNameContaining(String search, Pageable pageable); // 이름으로 검색

    @Query("SELECT p FROM Products p WHERE p.name=:search AND (p.gender=:gender OR p.gender=:every)")
    Page<Products> findByNameContainingAndGender(@Param("search") String search,@Param("gender") String gender,@Param("every") String every, Pageable pageable);
    // 이름, 성별

    @Query("select p from Products p WHERE p.name =:name AND p.brand =:brand AND p.color =:color AND p.size =:size")
    Optional<Products> duplicationChk(@Param("name") String name, @Param("brand") Brand brand
            , @Param("color") String color, @Param("size") int size);

    Page<Products> findByStockQuantityLessThan(int num, Pageable pageable); // 매진 임박 메서드

    @Query("SELECT p from Products p WHERE p.name LIKE %:searchKeyword% AND p.stockQuantity < :num")
    Page<Products> findByStockQuantityAndName(@Param("num") int num,@Param("searchKeyword")String searchKeyword,
                                              Pageable pageable); // 매진 임박 메서드

    @Query("SELECT p FROM Products p WHERE p.gender = :gender")
    Page<Products> findProductsByGender(@Param("gender") String gender, Pageable pageable); // 성별로 구분

    @Query("SELECT p FROM Products p WHERE p.gender = :gender AND p.name LIKE %:keyword%")
    Page<Products> findProductsByGender(@Param("gender") String gender,@Param("keyword") String keyword ,Pageable pageable); // 성별로 구분

    @Query("SELECT p FROM Products p WHERE p.gender= :gender AND p.name= :name AND p.brand=:brand AND p.color =:color")
    List<Products> findProductsToSize(@Param("gender") String gender,@Param("name")String name, @Param("brand")Brand brand,@Param("color")String color);

    @Query("SELECT p FROM Products p WHERE p.name= :name AND p.color=:color AND p.size=:size AND p.brand =:brand")
    Optional<Products> findProductsByNameAndColorAndSizeAndBrand(@Param("name") String name,@Param("color") String color, @Param("size") int size,@Param("brand") Brand brand);

    // 브랜드 O 검색 키워드 O
    @Query("SELECT p FROM Products p WHERE p.name LIKE %:name% AND p.brand= :brand")
    Page<Products> findProductsByNameAndBrand(@Param("name")String name, @Param("brand")Brand brand,Pageable pageable);

    // 브랜드 O 검색 키워드 O 성별 O
    @Query("SELECT p FROM Products p WHERE p.name LIKE %:name% AND p.brand= :brand AND (p.gender=:gender OR p.gender =:every)")
    Page<Products> findProductsByNameAndBrandAndGender(@Param("name")String name, @Param("brand")Brand brand,@Param("gender") String gender,@Param("every") String every,Pageable pageable);

    // 브랜드 O 검색 키워드 X
//    @Query("SELECT p FROM Products p WHERE p.brand= :brand")
    Page<Products> findProductsByBrand(Brand brand,Pageable pageable);


    // 브랜드 O 검색 키워드 X 성별 O
    @Query("SELECT p FROM Products p WHERE p.brand= :brand AND (p.gender=:gender OR p.gender=:every )")
    Page<Products> findProductsByBrandAndGender(@Param("brand") Brand brand,@Param("gender")String gender, @Param("every")String every ,Pageable pageable);

    @Query("SELECT p FROM Products p WHERE p.name LIKE %:name% AND p.member=:member") // 장바구니 페이지 같음?
    Page<Products> findProductsByNameAndMember(@Param("name")String nameSearch, @Param("member") Member member,Pageable pageable);

    Page<Products> findProductsByMember(Member member, Pageable pageable);

    @Query("SELECT p FROM Products p WHERE p.gender=:gender OR p.gender=:every") // 성별 조건만 있을 때
    Page<Products> findProductsByMemberAndGender(@Param("gender") String gender,@Param("every") String every,Pageable pageable);

}
