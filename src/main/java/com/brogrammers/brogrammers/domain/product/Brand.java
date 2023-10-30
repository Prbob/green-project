package com.brogrammers.brogrammers.domain.product;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class Brand {
    @Id @GeneratedValue
    @Column(name="brand_id")
    private Long id;

    @Column(name="brand_name")
    private String name;

    @OneToMany(mappedBy = "brand")
    private List<Products> products;

    public void setName(String name) {
        this.name = name;
    }

    @Builder
    public Brand(String name){
        this.name=name;
    }
}
