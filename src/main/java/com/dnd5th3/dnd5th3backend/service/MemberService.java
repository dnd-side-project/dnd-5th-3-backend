package com.dnd5th3.dnd5th3backend.service;

import com.dnd5th3.dnd5th3backend.config.security.jwt.JwtTokenProvider;
import com.dnd5th3.dnd5th3backend.controller.dto.member.MemberReissueTokenResponseDto;
import com.dnd5th3.dnd5th3backend.controller.dto.member.MemberRequestDto;
import com.dnd5th3.dnd5th3backend.domain.member.Member;
import com.dnd5th3.dnd5th3backend.exception.TokenException;
import com.dnd5th3.dnd5th3backend.repository.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    public Member saveMember(MemberRequestDto memberRequestDto) {
        String encode = passwordEncoder.encode(memberRequestDto.getPassword());
        Member member = Member.create(memberRequestDto, encode);
        return memberRepository.save(member);
    }

    public MemberReissueTokenResponseDto reissueAccessToken(MemberRequestDto memberRequestDto){
        Member member = memberRepository.findByEmail(memberRequestDto.getEmail());
        String refreshToken = member.getRefreshToken();
        String reissueAccessToken;

        if(memberRequestDto.getRefreshToken().equals(refreshToken)){
            if(jwtTokenProvider.isVaildToken(refreshToken)){
              reissueAccessToken = jwtTokenProvider.createAccessToken(member);
            }else {
                throw new TokenException();
            }
        }else {
            throw new IllegalArgumentException("refreshToken이 유효하지 않습니다.");
        }

        return  MemberReissueTokenResponseDto.builder()
                .email(member.getEmail())
                .accessToken(reissueAccessToken)
                .build();
    }

}
