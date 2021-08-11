package com.dnd5th3.dnd5th3backend.domain.member;

import com.dnd5th3.dnd5th3backend.controller.dto.member.MemberRequestDto;
import com.dnd5th3.dnd5th3backend.domain.common.BaseTime;
import lombok.*;

import javax.persistence.*;

@Builder
@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Member extends BaseTime {
    @Id
    @Column(name = "member_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String email;

    private String password;

    private String name;

    @Enumerated(value = EnumType.STRING)
    private MemberType memberType;

    @Enumerated(value = EnumType.STRING)
    private Role role;

    @Setter
    private String refreshToken;

    public static Member create(MemberRequestDto memberRequestDto,String encodePassword){

        return Member.builder()
                .email(memberRequestDto.getEmail())
                .password(encodePassword)
                .name(memberRequestDto.getName())
                .memberType(MemberType.GENERAL)
                .role(Role.ROLE_USER)
                .build();
    }
}
