package com.umc._th_workbook.config.security;

import com.umc._th_workbook.domain.member.entity.Member;
import com.umc._th_workbook.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Member member = memberRepository.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("해당 유저 네임을 가진 유저가 존재하지 않습니다: " + username));

        return org.springframework.security.core.userdetails.User
            .withUsername(member.getUsername())
            .password(member.getPassword())
            .roles(member.getRole().name())  // 역할 설정
            .build();
    }
}

