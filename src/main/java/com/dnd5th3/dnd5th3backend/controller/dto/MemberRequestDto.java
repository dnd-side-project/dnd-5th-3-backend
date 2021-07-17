package com.dnd5th3.dnd5th3backend.controller.dto;

import lombok.Getter;

@Getter
public class MemberRequestDto {
   private String email;
   private String name;
   private String password;
}
