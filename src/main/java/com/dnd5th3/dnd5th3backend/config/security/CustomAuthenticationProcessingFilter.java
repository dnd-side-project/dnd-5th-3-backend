package com.dnd5th3.dnd5th3backend.config.security;

import com.dnd5th3.dnd5th3backend.controller.dto.member.MemberRequestDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CustomAuthenticationProcessingFilter extends AbstractAuthenticationProcessingFilter {

    private ObjectMapper objectMapper = new ObjectMapper();

    protected CustomAuthenticationProcessingFilter() {
        super(new AntPathRequestMatcher("/api/member/login"));
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException{
        MemberRequestDto memberRequestDto = objectMapper.readValue(request.getReader(), MemberRequestDto.class);
        if(memberRequestDto == null){
            throw new IllegalArgumentException();
        }
        CustomAuthenticationToken authenticationToken = new CustomAuthenticationToken(memberRequestDto.getEmail(), memberRequestDto.getPassword());

        return getAuthenticationManager().authenticate(authenticationToken);
    }
}
