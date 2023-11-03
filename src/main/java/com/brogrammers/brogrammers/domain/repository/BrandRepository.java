package com.brogrammers.brogrammers.domain.repository;

import com.brogrammers.brogrammers.domain.product.Brand;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface BrandRepository  extends JpaRepository<Brand,Long> {
    Optional<Brand> findOneByName(String name); // 이름으로 브랜드 찾기

    Brand findOneById(Long id);


    @Query("SELECT b FROM Brand b WHERE b.name LIKE %:name%")
    Page<Brand> findBrandsByName(@Param("name") String name, Pageable pageable);
}
