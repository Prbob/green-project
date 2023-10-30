package com.brogrammers.brogrammers.domain.order;

import com.brogrammers.brogrammers.domain.member.Address;

import javax.persistence.*;

@Entity
public class Delivery {
    @Id @GeneratedValue
    @Column(name="delivery_id")
    private Long id;

    @Column(name="delivery_name")
    private String name; // 배송지명

    @OneToOne(mappedBy = "delivery")
    private Orders orders;

    @Embedded
    @Column(name="member_address")
    private Address address; // 주소
    public void saveAddress(Address address){ //
        this.address = address;
    }
//    private DeliveryStatus status;
}
