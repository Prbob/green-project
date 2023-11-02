package com.brogrammers.brogrammers.domain.product;

import com.brogrammers.brogrammers.domain.member.Member;
import com.brogrammers.brogrammers.domain.order.OrderProducts;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class Products {

    @Id @GeneratedValue
    @Column(name="products_id")
    private Long id; // ID, DB에서 자동 주입

    @Column(name="products_name")
    private String name; // 상품 이름,
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="brand_id")
    private Brand brand; // 상품 브랜드
    @Column(name="products_color")
    private String color; // 색상
    @Column(name="products_price", columnDefinition = "integer default 0")
    private int price; // 가격
    @Column(name="products_size")
    private int size; // 사이즈
    @Column(name="products_stockQuantity")
    private int stockQuantity;
    @Column(name="products_img_path")
    private String imgPath; // 썸네일 이미지 저장 경로
    @Column(name="products_img_name")
    private String imgName; // 썸네일 이미지 이름

    @Column(name="products_gender", columnDefinition = "varchar(255) default 'every'")
    private String gender;

    public void setStockQuantity(int orderStock){
        stockQuantity = orderStock;
    }
    public void saveImg(String imgPath, String imgName){
        this.imgPath = imgPath;
        this.imgName = imgName;
    }

    @Column(name="products_order_count")
    private Long orderCount;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="member_id")
    private Member member;


    @OneToMany(mappedBy = "products")
    private List<Imgs> imgs; // 이미지 저장 테이블

    public Products saveProduct(String name, Brand brand,String color ,int price, int size,
                                int stockQuantity,Member member,String gender){
        this.name = name;
        this.brand = brand;
        this.color = color;
        this.price = price;
        this.size = size;
        this.member = member;
        this.stockQuantity = stockQuantity;
        this.gender = gender;
        return this;
    } // 상품 저장할 때 사용하는

    @Column(name="reg_LocalDate")
    private LocalDate regDate; // 자동 주입되게

    @PrePersist
    public void prePersist() {
        // 엔티티가 영구 저장되기 전에 실행되는 메서드
        this.regDate = LocalDate.now(); // 현재 날짜와 시간을 설정
    }

    @OneToMany(mappedBy = "products") // 다대다 관계 아직 덜 공부됨.
    List<ProductsCategory> productsCategoryList = new ArrayList<>();

    @OneToMany(mappedBy = "products")
    List<OrderProducts> orderProducts = new ArrayList<>();

    @OneToMany(mappedBy = "products")
    private List<BasketProducts> baskets;
}
