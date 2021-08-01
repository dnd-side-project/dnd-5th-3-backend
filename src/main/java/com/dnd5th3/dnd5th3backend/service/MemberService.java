package com.dnd5th3.dnd5th3backend.service;

import com.dnd5th3.dnd5th3backend.domain.member.Member;

public interface MemberService {
    Member findMemberById(Long id);
}
