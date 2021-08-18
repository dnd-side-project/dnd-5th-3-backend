package com.dnd5th3.dnd5th3backend.repository.member;

import com.dnd5th3.dnd5th3backend.domain.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member,Long> {
    Member findByEmail(String email);
    boolean existsByEmail(String email);
    boolean existsByName(String name);
}
