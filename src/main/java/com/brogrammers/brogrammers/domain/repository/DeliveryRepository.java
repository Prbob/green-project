package com.brogrammers.brogrammers.domain.repository;

import com.brogrammers.brogrammers.domain.order.Delivery;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeliveryRepository extends JpaRepository<Delivery,Long> {

}
