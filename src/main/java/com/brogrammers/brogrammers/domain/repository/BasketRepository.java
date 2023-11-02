package com.brogrammers.brogrammers.domain.repository;

import com.brogrammers.brogrammers.domain.member.Member;
import com.brogrammers.brogrammers.domain.order.Basket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface BasketRepository extends JpaRepository<Basket, Long> {

    @Query("SELECT b FROM Basket b WHERE b.member = :member")
    Optional<Basket> findBasketByMember(@Param("member") Member member);

}
