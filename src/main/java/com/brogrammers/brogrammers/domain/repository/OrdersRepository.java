package com.brogrammers.brogrammers.domain.repository;

import com.brogrammers.brogrammers.domain.member.Member;
import com.brogrammers.brogrammers.domain.order.Orders;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface OrdersRepository extends JpaRepository<Orders,Long> {
    @Query("SELECT o FROM Orders o WHERE o.id =:id")
    Optional<Orders> findOrdersById(@Param("id") Long id);

    Page<Orders> findOrdersByMember(Member member, Pageable pageable);
}
