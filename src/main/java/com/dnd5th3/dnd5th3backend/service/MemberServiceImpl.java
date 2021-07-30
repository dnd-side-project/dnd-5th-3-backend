package com.dnd5th3.dnd5th3backend.service;

import com.dnd5th3.dnd5th3backend.domain.member.Member;
import com.dnd5th3.dnd5th3backend.exception.MemberNotFoundException;
import com.dnd5th3.dnd5th3backend.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;

    public Member findMemberById(Long id) {
        return memberRepository.findById(id).orElseThrow(() -> new MemberNotFoundException("존재하지 않는 회원입니다."));
    }
}
