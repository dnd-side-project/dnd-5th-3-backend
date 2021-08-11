package com.dnd5th3.dnd5th3backend.controller.dto.member;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberReissueTokenResponseDto {
    private String email;
    private String accessToken;
}
