package com.dnd5th3.dnd5th3backend.config.security;

import com.dnd5th3.dnd5th3backend.controller.dto.MemberDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CustomLoginProcessingFilter extends AbstractAuthenticationProcessingFilter {

    private ObjectMapper objectMapper = new ObjectMapper();

    protected CustomLoginProcessingFilter() {
        super(new AntPathRequestMatcher("/api/member/login"));
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException{
        MemberDto memberDto = objectMapper.readValue(request.getReader(), MemberDto.class);
        if(memberDto == null){
            throw new IllegalArgumentException();
        }
        CustomAuthenticationToken authenticationToken = new CustomAuthenticationToken(memberDto.getEmail(),memberDto.getPassword());

        return getAuthenticationManager().authenticate(authenticationToken);
    }
}
