package com.dnd5th3.dnd5th3backend.controller.dto.member;

import com.dnd5th3.dnd5th3backend.domain.member.MemberType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class MemberResponseDto {
    private long memberId;
    private String email;
    private String name;
    private MemberType memberType;
}
