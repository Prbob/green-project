package com.brogrammers.brogrammers.domain.repository;

import com.brogrammers.brogrammers.domain.order.Delivery;
import com.brogrammers.brogrammers.domain.order.Orders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface DeliveryRepository extends JpaRepository<Delivery,Long> {


}
