package com.brogrammers.brogrammers.domain.repository;

import com.brogrammers.brogrammers.domain.board.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    @Query("SELECT r FROM Review r WHERE r.productName =:name")
    Page<Review> findAllByProductName(@Param("name") String name, Pageable pageable);

}
