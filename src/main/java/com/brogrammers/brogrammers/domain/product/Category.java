package com.brogrammers.brogrammers.domain.product;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class Category {
    @Id @GeneratedValue
    private Long id;
    private String name;

    @Builder
    public Category(String name){
        this.name = name;
    }
    public void setName(String name){this.name=name;}

    @OneToMany(mappedBy = "category")
    private List<ProductsCategory> productsCategoryList = new ArrayList<>();
}