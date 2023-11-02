package com.brogrammers.brogrammers.domain.member;


import com.brogrammers.brogrammers.domain.board.Board;
import com.brogrammers.brogrammers.domain.order.Basket;
import com.brogrammers.brogrammers.domain.order.Orders;
import com.brogrammers.brogrammers.domain.product.Products;
import lombok.Getter;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter  // @Data : getter setter
@Entity  // JPA 테이블 만들어줘 어노테이션
@DynamicInsert
public class Member {  /////////////////// 완료 !

    @Id @GeneratedValue // 시퀀스? DB 너가 알아서 넣어줘.  @Id == PK
    @Column(name="member_id") //
    private Long id;
    @Column(name="member_email")
    @NotNull // 꼭 넣어라.
    private String email; // 이메일(아이디)
    @NotNull
    @Column(name="member_pwd")
    @NotNull
    private String pwd; // 비밀번호
    @NotNull
    @Column(name="member_name")
    private String name; // 회원 이름
    @Column(name="member_phone",columnDefinition = "varchar(255) default '010-1234-5678'")
    private String phone;


    @Column(name="member_img_path")
    private String imgPath; // 썸네일 이미지 저장 경로
    @Column(name="member_img_name")
    private String imgName; // 썸네일 이미지 이름

    public void saveImg(String imgPath, String imgName){
        this.imgPath = imgPath;
        this.imgName = imgName;
    }

    public Member saveMember(String email, String pwd, String name){  // 회원 가입 때 쓸 메서드
        this.email = email;
        this.pwd = pwd;
        this.name = name;
        return this;
    }
    @Embedded
    @Column(name="member_address")
    private Address address; // 주소
    public void saveAddress(Address address){ //
        this.address = address;
    }

    @Column(columnDefinition = "varchar(255) default 'NORMAL'")
    private String accessrigths; // 회원 권한, 처음 생성할 때 권한 부여X

    @Column(name="member_order_count",columnDefinition = "integer default 0")
    private Long orderCount; /// 총 주문 횟수
    @Column(name="member_grade")
    private String grade; // 회원 등급, 주문 횟수에 따른 등급조정?

    @Column(name="member_registrationDate")
    private LocalDate registrationDate; // 가입날짜?, 자동 주입


    @OneToMany(mappedBy = "member")
    private List<Orders> orders = new ArrayList<>(); // 오더와 1대다 관계

    @OneToMany(mappedBy = "member")
    private List<Products> produtcs = new ArrayList<>(); // 상품과 1대다 관계

    @OneToMany(mappedBy = "member")
    private List<Board> boards = new ArrayList<>();

    @PrePersist
    public void prePersist() {
        // 엔티티가 영구 저장되기 전에 실행되는 메서드
        this.registrationDate = LocalDate.now(); // 현재 날짜와 시간을 설정
    }

    @OneToOne(mappedBy = "member")
    private Basket basket;
    public void saveBasket(Basket basket){
        this.basket = basket;
    }
}
