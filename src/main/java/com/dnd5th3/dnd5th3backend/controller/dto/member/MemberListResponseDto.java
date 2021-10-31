package com.dnd5th3.dnd5th3backend.controller.dto.member;

import com.dnd5th3.dnd5th3backend.domain.member.MemberType;
import com.dnd5th3.dnd5th3backend.domain.member.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class MemberListResponseDto {

    private List<MemberDto> memberDtoList;
    private int pageNum;
    private int totalPage;
    private long totalCount;

    @Setter
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MemberDto {

        private Long id;
        private String email;
        private String name;
        private MemberType memberType;
        private Role role;
        private LocalDateTime createdDate;
    }
}