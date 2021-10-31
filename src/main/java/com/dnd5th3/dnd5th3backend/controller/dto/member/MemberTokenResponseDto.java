package com.dnd5th3.dnd5th3backend.controller.dto.member;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MemberTokenResponseDto {

    private String email;
    private String name;
    private String accessToken;
    private String refreshToken;

}
