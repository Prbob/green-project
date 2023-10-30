package com.brogrammers.brogrammers.domain.order;

import com.brogrammers.brogrammers.domain.member.Member;
import com.brogrammers.brogrammers.domain.product.BasketProducts;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class Basket { // 장바구니
    @Id @GeneratedValue
    @Column(name="basket_id")
    private Long id;

    @Builder
    public Basket(Member member) {
        this.member = member;
    }

    @OneToOne
    @JoinColumn(name="member_id")
    private Member member;

    @OneToMany(mappedBy = "basket")
    private List<BasketProducts> basketProducts;


}
