package com.brogrammers.brogrammers.domain.service;

import com.brogrammers.brogrammers.domain.order.Delivery;
import com.brogrammers.brogrammers.domain.repository.DeliveryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
@RequiredArgsConstructor
public class DeliveryService {
    private final DeliveryRepository deliveryRepository;

    public Long save(Delivery delivery){
        deliveryRepository.save(delivery);
        return delivery.getId();
    }
}
