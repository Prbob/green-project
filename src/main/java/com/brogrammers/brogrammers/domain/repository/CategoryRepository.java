package com.brogrammers.brogrammers.domain.repository;

import com.brogrammers.brogrammers.domain.product.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category,Long> {
    Optional<Category> findByName(String name);
}
