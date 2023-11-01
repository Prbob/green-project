package com.brogrammers.brogrammers.domain.repository;

import com.brogrammers.brogrammers.domain.order.OrderProducts;
import com.brogrammers.brogrammers.domain.order.Orders;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderProductsRepository extends JpaRepository<OrderProducts,Long> {

    List<OrderProducts> findOrderProductsByOrders(Orders orders);

}