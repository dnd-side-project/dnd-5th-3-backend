package com.dnd5th3.dnd5th3backend.config.security.oauth2;

import com.dnd5th3.dnd5th3backend.domain.member.Member;
import com.dnd5th3.dnd5th3backend.domain.member.MemberType;
import com.dnd5th3.dnd5th3backend.domain.member.Role;
import com.dnd5th3.dnd5th3backend.repository.member.MemberRepository;
import com.dnd5th3.dnd5th3backend.utils.RandomNumber;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.OAuth2ErrorCodes;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class CustomOAuth2UserServiceImpl implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final MemberRepository memberRepository;
    private PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2UserService<OAuth2UserRequest, OAuth2User> oAuth2UserService = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = oAuth2UserService.loadUser(userRequest);
        String email = (String) oAuth2User.getAttributes().get("email");

        if(!memberRepository.existsByEmail(email)){
            String name = null;
            boolean isExistName = true;

            while (isExistName){
                name = RandomNumber.generateName();
                isExistName = memberRepository.existsByName(name);
            }

            String password = passwordEncoder.encode(RandomNumber.generatePassword());

            Member member = Member.builder()
                    .email(email)
                    .name(name)
                    .password(password)
                    .role(Role.ROLE_USER)
                    .memberType(MemberType.SOCIAL)
                    .build();
            memberRepository.save(member);

        }else {

            Member member = memberRepository.findByEmail(email);
            if(!MemberType.SOCIAL.equals(member.getMemberType())){
                throw new OAuth2AuthenticationException(new OAuth2Error(OAuth2ErrorCodes.ACCESS_DENIED),"Oauth2 접근 오류 ["+email+"], MemberType ["+member.getMemberType().name()+"]");
            }

        }
        return oAuth2User;
    }

}
