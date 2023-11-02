package com.brogrammers.brogrammers.domain.service;

import com.brogrammers.brogrammers.domain.member.Member;
import com.brogrammers.brogrammers.domain.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional(readOnly = true) // @Transactional 이 때 DB에 저장을 해줌.
@RequiredArgsConstructor // 생성자 만들주는건데 , final이 붙은 필드를 매개변수로 받는 생성자를 생성
public class MemberService {

    private final MemberRepository memberRepository;

    @Transactional
    public Long join(Member member){
        memberRepository.save(member); // 영속성
        return member.getId();
    }

    public Member findById(Long id){
        return memberRepository.findById(id).get();
    }
    // 회원 중복 확인 메서드
    public Optional<Member> validataDuplicateMember(String email) {
        return memberRepository.findByEmail(email);
    }

    public Optional<Member> findByEmail(String email){return memberRepository.findByEmail(email);}

}
