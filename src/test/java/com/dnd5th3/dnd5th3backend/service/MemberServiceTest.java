package com.dnd5th3.dnd5th3backend.service;

import com.dnd5th3.dnd5th3backend.config.QuerydslConfig;
import com.dnd5th3.dnd5th3backend.config.TestConfig;
import com.dnd5th3.dnd5th3backend.config.security.CustomUserDetailServiceImpl;
import com.dnd5th3.dnd5th3backend.config.security.jwt.JwtTokenProvider;
import com.dnd5th3.dnd5th3backend.controller.dto.member.MemberReissueTokenResponseDto;
import com.dnd5th3.dnd5th3backend.controller.dto.member.MemberRequestDto;
import com.dnd5th3.dnd5th3backend.domain.member.Member;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Import({MemberService.class, QuerydslConfig.class,TestConfig.class,JwtTokenProvider.class, CustomUserDetailServiceImpl.class})
@ActiveProfiles("h2")
class MemberServiceTest {

    @Autowired
    private MemberService memberService;

    @DisplayName("회원 등록 테스트")
    @Test
    void saveMember() {
        String email = "member@moomool.com";
        String name = "spring";
        String password = "1234";
        MemberRequestDto memberRequestDto = new MemberRequestDto(email, name, password,null,null);
        Member member = memberService.saveMember(memberRequestDto);
        assertNotNull(member.getId(),"회원 정상등록 확인");
    }
    
    @DisplayName("AccessToken 재발급 테스트")
    @Test
    void reissueAccessToken() {

        String email = "test@naver.com";
        String refreshToken = "eyJhbGciOiJIUzI1NiJ9.eyJjbGFpbSI6eyJyZWZyZXNoIjoiYzNhOGJlNGQtNjAxYS00YjY0LWE3NWMtYmVhY2U3ZTAzMjExIn0sImV4cCI6MTYyOTY5MDY3NX0.EgqcB0chYYTAx7VDUTqeMC-sV_0veGr7QOrFc4Bo8ig";

        MemberRequestDto memberRequestDto = new MemberRequestDto(email, null, null,null,refreshToken);
        MemberReissueTokenResponseDto reissueTokenResponseDto = memberService.reissueAccessToken(memberRequestDto);
        assertNotNull(reissueTokenResponseDto.getAccessToken());
    }

    @DisplayName("이메일 중복확인 테스트")
    @Test
    void isExistsEmail() {
        String email = "test@gmail.com";
        boolean existsEmail = memberService.isExistsEmail(email);
        assertTrue(existsEmail," 이메일 중복 확인");
    }

    @DisplayName("닉네임 중복확인 테스트")
    @Test
    void isExistsName() {
        String name = "moomool";
        boolean existsEmail = memberService.isExistsName(name);
        assertTrue(existsEmail," 닉네임 중복 확인");
    }

}