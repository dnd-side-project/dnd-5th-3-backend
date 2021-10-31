package com.dnd5th3.dnd5th3backend.repository.member;

import com.dnd5th3.dnd5th3backend.domain.member.Member;
import com.dnd5th3.dnd5th3backend.domain.member.MemberType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MemberRepository extends JpaRepository<Member,Long> {
    Member findByEmail(String email);
    boolean existsByEmail(String email);
    boolean existsByName(String name);
    List<Member> findAllByMemberType(MemberType memberType);
}
