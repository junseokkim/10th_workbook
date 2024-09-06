package com.umc._th_workbook.config.security;

import com.umc._th_workbook.domain.member.entity.Member;
import com.umc._th_workbook.domain.member.entity.Role;
import com.umc._th_workbook.domain.member.repository.MemberRepository;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        // 사용자 정보 확인
        Map<String, Object> attributes = oAuth2User.getAttributes();
        Map<String, Object> properties = (Map<String, Object>) attributes.get("properties");

        // 닉네임 가져오기
        String nickname = (properties != null && properties.get("nickname") != null) ? properties.get("nickname").toString() : "Unknown User";

        Map<String, Object> modifiedAttributes = new HashMap<>(attributes);
        modifiedAttributes.put("nickname", nickname);

        // 사용자 정보 저장 또는 업데이트
        saveOrUpdateUser(nickname);

        // 닉네임을 Principal로 사용하기 위해 DefaultOAuth2User에 닉네임 설정
        return new DefaultOAuth2User(
            oAuth2User.getAuthorities(),
            modifiedAttributes,  // 수정된 attributes 사용
            "nickname"  // nickname을 Principal로 설정
        );
    }

    private void saveOrUpdateUser(String nickname) {
        String fixedPassword = passwordEncoder.encode("SOCIAL_USER"); // 소셜 로그인 유저는 고정 패스워드 사용

        if (memberRepository.findByUsername(nickname).isEmpty()) {
            Member member = Member.builder()
                .username(nickname)
                .password(fixedPassword)
                .role(Role.USER)
                .build();
            memberRepository.save(member);
        }
    }
}
