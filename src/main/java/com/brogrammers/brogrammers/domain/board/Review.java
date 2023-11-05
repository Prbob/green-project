package com.brogrammers.brogrammers.domain.board;

import com.brogrammers.brogrammers.domain.member.Member;
import lombok.Getter;

import javax.persistence.*;
import java.time.LocalDate;

@Getter
@Entity
public class Review {
    @Id @GeneratedValue
    @Column(name="board_id")
    private Long id;
    @Column(name="board_content")
    private String content; // 게시글 내용

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="member_id")
    private Member member; // 누가 작성했는지.

    @Column(name="board_product_name")
    private String productName;

    @Column(name="board_registrationDate")
    private LocalDate registrationDate; // 게시글 작성날짜
    @PrePersist
    public void prePersist() {
        // 엔티티가 영구 저장되기 전에 실행되는 메서드
        this.registrationDate = LocalDate.now(); // 현재 날짜와 시간을 설정
    }



}
