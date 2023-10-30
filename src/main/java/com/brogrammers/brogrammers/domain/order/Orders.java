package com.brogrammers.brogrammers.domain.order;

import com.brogrammers.brogrammers.domain.member.Address;
import com.brogrammers.brogrammers.domain.member.Member;
import lombok.Getter;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter // @Data == @Getter @Setter
public class Orders {
    @Id @GeneratedValue
    @Column(name="orders_id")
    private Long id;

    @Column(name="total_price")
    private int totalPrice;

    @Column(name="payment")
    private String payment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="member_id")
    private Member member; // 회원 정보

    @Column(name="order_name")
    private String name; // 주문한 사람
    @Column(name="order_phone")
    private String phone;
    @Column(name="order_phone2")
    private String phone2;
    @Column(name="order_phone3")
    private String phone3;


    @Column(name="order_registrationDate")
    private LocalDate registrationDate; // 주문날짜


    @PrePersist
    public void prePersist() {
        // 엔티티가 영구 저장되기 전에 실행되는 메서드
        this.registrationDate = LocalDate.now(); // 현재 날짜와 시간을 설정
    }
    @OneToMany(mappedBy = "orders")
    private List<OrderProducts> orderProducts = new ArrayList<>();

    @Column(name="order_status", columnDefinition = "varchar(255) default 'NO_PAYMENY'")
    private OrderStatus orderStatus; // 주문 상태

    @OneToOne
    @JoinColumn(name="dilivery_id")
    private Delivery delivery;
}
