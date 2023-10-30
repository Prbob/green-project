package com.brogrammers.brogrammers.web.products;

import com.brogrammers.brogrammers.domain.member.Member;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
public class ProductForm {
    @NotEmpty(message = "브랜드 기입은 필수 입니다.")
    private String productBrand;
    private String productCategory;
    @NotEmpty(message = "상품명 기입은 필수 입니다.")
    private String productName;
    @NotEmpty(message = "색상 기입은 필수 입니다.")
    private String productColor;
    @NotNull(message = "사이즈 기입은 필수 입니다.")
    private int size;
    @NotNull(message = "수량 기입은 필수 입니다.")
    @Min(value = 0,message = "입력가능한 최소 수량은 0개부터 입니다.")
    private int stockQuantity;
    @NotNull(message = "가격 기입은 필수 입니다.")
    private int price;
    private Member member; // 등록 회원이 누구인지 식별하기 위한 회원
    private String gender; // 성별구분

    @Builder
    public ProductForm(String productBrand, String productName, String productColor,
                       int size, int stockQuantity, int price, MultipartFile[] imgs, MultipartFile img,String gender) {
        this.productBrand = productBrand;
        this.productName = productName;
        this.productColor = productColor;
        this.size = size;
        this.stockQuantity = stockQuantity;
        this.price = price;
        this.imgs = imgs;
        this.img = img;
        this.gender = gender;
    }

    // 사진 등록
    private MultipartFile img;
    private MultipartFile[] imgs; // html -> Controller 파일 담는 용도

}
