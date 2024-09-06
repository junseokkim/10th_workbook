package com.umc._th_workbook.domain.member.service;

import com.umc._th_workbook.domain.member.entity.Member;
import com.umc._th_workbook.domain.member.entity.Role;
import com.umc._th_workbook.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public void registerNewMember(String username, String password, Role role) {

        if (memberRepository.findByUsername(username).isPresent()) {
            throw new IllegalArgumentException("이미 존재하는 유저네임입니다.");
        }

        Member member = Member.builder()
            .username(username)
            .password(passwordEncoder.encode(password))
            .role(role)
            .build();

        memberRepository.save(member);
    }
}
