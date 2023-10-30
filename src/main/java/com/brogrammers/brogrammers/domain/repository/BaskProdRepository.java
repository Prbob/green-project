package com.brogrammers.brogrammers.domain.repository;

import com.brogrammers.brogrammers.domain.order.Basket;
import com.brogrammers.brogrammers.domain.product.BasketProducts;
import com.brogrammers.brogrammers.domain.product.Products;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface BaskProdRepository extends JpaRepository<BasketProducts,Long> {

    @Query("select b.products FROM BasketProducts b WHERE b.basket =:basket ")
    List<Products> findProductsByBasket(@Param("basket") Basket basket);
    @Query("SELECT bp FROM BasketProducts bp WHERE bp.basket = :basket AND bp.products = :products")
    Optional<BasketProducts> findOneByBaskeyAndProducts(@Param("basket") Basket basket , @Param("products") Products products);

    void deleteBasketProductsById(Long id);
}
