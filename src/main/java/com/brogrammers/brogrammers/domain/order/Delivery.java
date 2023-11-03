package com.brogrammers.brogrammers.domain.order;

import com.brogrammers.brogrammers.domain.member.Address;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class Delivery {
    @Id @GeneratedValue
    @Column(name="delivery_id")
    private Long id;

    @OneToOne(mappedBy = "delivery")
    private Orders orders;

    @Embedded
    @Column(name="member_address")
    private Address address; // 주소
    @Column(name="delivery_status")
    private DeliveryStatus deliveryStatus = DeliveryStatus.READEY;

    public void setAddress(Address address) {
        this.address = address;
    }

    public Delivery(DeliveryStatus deliveryStatus) {
        this.deliveryStatus = deliveryStatus;
    }

    public void saveAddress(Address address){ //
        this.address = address;
    }
}
