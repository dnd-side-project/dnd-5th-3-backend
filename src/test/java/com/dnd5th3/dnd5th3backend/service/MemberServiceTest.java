package com.dnd5th3.dnd5th3backend.service;

import com.dnd5th3.dnd5th3backend.config.QuerydslConfig;
import com.dnd5th3.dnd5th3backend.config.TestConfig;
import com.dnd5th3.dnd5th3backend.config.security.CustomUserDetailServiceImpl;
import com.dnd5th3.dnd5th3backend.config.security.jwt.JwtTokenProvider;
import com.dnd5th3.dnd5th3backend.controller.dto.member.MemberReissueTokenResponseDto;
import com.dnd5th3.dnd5th3backend.controller.dto.member.MemberRequestDto;
import com.dnd5th3.dnd5th3backend.domain.member.Member;
import com.dnd5th3.dnd5th3backend.repository.member.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Import({MemberService.class, QuerydslConfig.class,TestConfig.class,JwtTokenProvider.class, CustomUserDetailServiceImpl.class})
@ActiveProfiles("h2")
class MemberServiceTest {

    @Autowired
    private MemberService memberService;

    @Autowired
    private MemberRepository memberRepository;

    private Member member;

    @BeforeEach
    void setup(){
        long memberId = 1;
        member = memberRepository.getById(memberId);
    }

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
    
//    @DisplayName("AccessToken 재발급 테스트")
//    @Rollback(value = false)
//    @Test
//    void reissueAccessToken() {
//
//        String email = "token@naver.com";
//        String refreshToken = "eyJhbGciOiJIUzI1NiJ9.eyJjbGFpbSI6eyJyZWZyZXNoIjoiYWUxN2IyYzgtMmZhZS00ODUyLTg2MzItZTAyNjVmYzNjZDgzIn0sImV4cCI6MjgzOTU4NDM4OX0.4TuG0Kgejk4bzKv_LWizeoToM3JqZN68spRtvRyqfpY";
//
//        MemberRequestDto memberRequestDto = new MemberRequestDto(email, null, null,null,refreshToken);
//        MemberReissueTokenResponseDto reissueTokenResponseDto = memberService.reissueAccessToken(memberRequestDto);
//        assertNotNull(reissueTokenResponseDto.getAccessToken());
//    }

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

    @DisplayName("비밀번호 확인 테스트")
    @Test
    void isCollectPassword() {
        String password = "1234";
        boolean collectPassword = memberService.isCollectPassword(password, member);
        assertTrue(collectPassword," 비밀번호 일치 확인");
    }

    @DisplayName("프로필 변경 테스트")
    @Test
    void updateMember() {
        String name = "moomool2";
        MemberRequestDto memberRequestDto = new MemberRequestDto(null,name,null,null,null);
        Member member = memberService.updateMember(memberRequestDto, this.member);
        assertEquals(name,member.getName()," 닉네임 변경되었는지 확인");

        PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
        String password = "12345";
        name = "moomool3";
        memberRequestDto = new MemberRequestDto(null,name,password,null,null);
        member = memberService.updateMember(memberRequestDto, this.member);
        assertEquals(name,member.getName()," 닉네임 변경되었는지 확인");
        assertTrue(passwordEncoder.matches(password,member.getPassword())," 비밀번호 변경되었는지 확인");
    }

    @DisplayName("회원 탈퇴 테스트")
    @Test
    void deleteMember() {
        String email = "del@naver.com";
        long memberId = 4;
        Member mem = memberRepository.getById(memberId);
        MemberRequestDto memberRequestDto = new MemberRequestDto(email,null,null,null,null);
        Member member = memberService.deleteMember(memberRequestDto,mem);
        assertEquals(member.getEmail(), mem.getEmail()," 정상 삭제 확인");
    }
}