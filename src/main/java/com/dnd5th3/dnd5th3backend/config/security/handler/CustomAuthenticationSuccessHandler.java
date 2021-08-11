package com.dnd5th3.dnd5th3backend.config.security.handler;

import com.dnd5th3.dnd5th3backend.config.security.jwt.JwtTokenProvider;
import com.dnd5th3.dnd5th3backend.controller.dto.member.MemberTokenResponseDto;
import com.dnd5th3.dnd5th3backend.domain.member.Member;
import com.dnd5th3.dnd5th3backend.repository.member.MemberRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@RequiredArgsConstructor
@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private ObjectMapper objectMapper = new ObjectMapper();
    private final JwtTokenProvider jwtTokenProvider;
    private final MemberRepository memberRepository;
    private final ModelMapper modelMapper;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) throws IOException, ServletException {
        AuthenticationSuccessHandler.super.onAuthenticationSuccess(request, response, chain, authentication);
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        Member member;
        if(authentication.getPrincipal() instanceof DefaultOAuth2User){
            OAuth2User oAuth2User = (DefaultOAuth2User) authentication.getPrincipal();
            String email = (String) oAuth2User.getAttributes().get("email");
            member = memberRepository.findByEmail(email);
        }else {
            member =  (Member)authentication.getPrincipal();
        }

        String accessToken  = jwtTokenProvider.createAccessToken(member);
        String refreshToken = jwtTokenProvider.createRefreshToken(member);

        MemberTokenResponseDto memberResponseDto = modelMapper.map(member,MemberTokenResponseDto.class);
        memberResponseDto.setAccessToken(accessToken);
        memberResponseDto.setRefreshToken(refreshToken);

        response.setStatus(HttpStatus.OK.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.toString());

        objectMapper.writeValue(response.getWriter(),memberResponseDto);
    }
}
