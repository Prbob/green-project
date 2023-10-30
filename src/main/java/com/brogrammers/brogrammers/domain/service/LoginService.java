package com.brogrammers.brogrammers.domain.service;

import com.brogrammers.brogrammers.domain.member.Member;
import com.brogrammers.brogrammers.domain.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LoginService {
    private final MemberRepository memberRepository;

    public Member login(String memberEmail, String password){
        Optional<Member> byLoginId = memberRepository.findByEmail(memberEmail);
        return byLoginId.filter(m->m.getPwd().equals(password)).orElse(null);
    }

}
