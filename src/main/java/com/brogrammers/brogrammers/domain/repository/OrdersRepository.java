package com.brogrammers.brogrammers.domain.repository;

import com.brogrammers.brogrammers.domain.order.Orders;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OrdersRepository extends JpaRepository<Orders,Long> {
    Optional<Orders> findOrdersById(Long id);
}
