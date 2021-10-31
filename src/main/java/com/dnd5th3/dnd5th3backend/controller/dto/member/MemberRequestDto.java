package com.dnd5th3.dnd5th3backend.controller.dto.member;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Setter
public class MemberRequestDto {
   private String email;
   private String name;
   private String password;
   private String accessToken;
   private String refreshToken;
}
