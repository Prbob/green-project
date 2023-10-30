package com.brogrammers.brogrammers.domain.product;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Imgs { // 이미지 테이블
    @Id @GeneratedValue
    @Column(name="imgs_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY )
    @JoinColumn(name="products_id")
    private Products products; // 상품 외래키

    @Column(name="original_name") //원본 이름
    private String originName;
    @Column(name="save_name") // 저장 이름
    private String saveName;
    @Column(name="img_size")
    private Long size;
    @Column(name="img_path")
    private String path;

    @Builder
    public Imgs(String originName, String saveName, String path, Long size,Products products){
        this.originName = originName;
        this.saveName = saveName;
        this.path = path;
        this.size = size;
        this.products = products;
    }



}
