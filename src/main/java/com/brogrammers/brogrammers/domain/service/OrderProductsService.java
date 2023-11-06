package com.brogrammers.brogrammers.domain.service;

import com.brogrammers.brogrammers.domain.order.OrderProducts;
import com.brogrammers.brogrammers.domain.order.Orders;
import com.brogrammers.brogrammers.domain.product.Products;
import com.brogrammers.brogrammers.domain.repository.OrderProductsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Transactional
@RequiredArgsConstructor
@Service
public class OrderProductsService {
    private final OrderProductsRepository orderProductsRepository;

    public void save(OrderProducts orderProducts){
        orderProductsRepository.save(orderProducts);
    }
    public List<OrderProducts> findOrderproductsByOrders(Orders orders){
        return orderProductsRepository.findOrderProductsByOrders(orders);
    }
    public List<OrderProducts> findOrderProductsByProducts(Products products){
        return orderProductsRepository.findOrderProductsByProducts(products);
    }

    public Optional<OrderProducts> findOrderProductsByOrdersAndProducts(Orders orders , Products products){
        return orderProductsRepository.findOrderProductsByOrdersAndProducts(orders,products);
    }
}
