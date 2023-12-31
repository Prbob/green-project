package com.brogrammers.brogrammers.domain.repository;

import com.brogrammers.brogrammers.domain.order.OrderProducts;
import com.brogrammers.brogrammers.domain.order.Orders;
import com.brogrammers.brogrammers.domain.product.Products;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrderProductsRepository extends JpaRepository<OrderProducts,Long> {

    List<OrderProducts> findOrderProductsByOrders(Orders orders);

    List<OrderProducts> findOrderProductsByProducts(Products products);

    Optional<OrderProducts> findOrderProductsByOrdersAndProducts(Orders orders, Products products);
}
