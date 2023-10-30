package com.brogrammers.brogrammers.domain.repository;

import com.brogrammers.brogrammers.domain.product.Brand;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BrandRepository  extends JpaRepository<Brand,Long> {
    Optional<Brand> findOneByName(String name); // 이름으로 브랜드 찾기

    Brand findOneById(Long id);
}
