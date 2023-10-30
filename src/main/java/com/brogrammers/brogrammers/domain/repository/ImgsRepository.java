package com.brogrammers.brogrammers.domain.repository;

import com.brogrammers.brogrammers.domain.product.Imgs;
import com.brogrammers.brogrammers.domain.product.Products;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ImgsRepository extends JpaRepository<Imgs,Long> {


    List<Imgs> findImgsByProducts(Products products);

}
