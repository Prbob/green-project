package com.brogrammers.brogrammers.domain.service;

import com.brogrammers.brogrammers.domain.member.Member;
import com.brogrammers.brogrammers.domain.order.Basket;
import com.brogrammers.brogrammers.domain.repository.BasketRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class BasketService {

    private final MemberService memberService;
    private final BasketRepository basketRepository;
    public void save(Basket basket){
        basketRepository.save(basket);
    }
    public Basket findById(Long id){return basketRepository.findById(id).get();}

    public Optional<Basket> findByMemberid(Long id){  // 회원 아이디로 장바구니 찾기
        Member member = memberService.findById(id);
        return basketRepository.findBasketByMember(member);
    }
}
