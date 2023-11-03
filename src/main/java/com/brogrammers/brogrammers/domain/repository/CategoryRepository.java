package com.brogrammers.brogrammers.domain.repository;

import com.brogrammers.brogrammers.domain.product.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category,Long> {
    Optional<Category> findByName(String name);

    @Query("SELECT c FROM Category c WHERE c.name LIKE %:name%")
    Page<Category> findCategoryByName(@Param("name") String keyName, Pageable pageable);
}
