package com.dnd5th3.dnd5th3backend.controller.dto;

import com.dnd5th3.dnd5th3backend.domain.member.Member;
import com.dnd5th3.dnd5th3backend.domain.member.Role;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class MemberResponseDto {
    private String email;
    private String name;
    private Role role;
    private String accessToken;
    private String refreshToken;

    public static MemberResponseDto createMemberResponseDto(Member member,String accessToken,String refreshToken){
        return MemberResponseDto.builder()
                .email(member.getEmail())
                .name(member.getName())
                .role(member.getRole())
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }
}
