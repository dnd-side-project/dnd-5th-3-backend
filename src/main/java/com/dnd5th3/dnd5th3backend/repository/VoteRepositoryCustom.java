package com.dnd5th3.dnd5th3backend.repository;

import com.dnd5th3.dnd5th3backend.domain.member.Member;
import com.dnd5th3.dnd5th3backend.domain.posts.Posts;

public interface VoteRepositoryCustom {

    Boolean currentMemberVoted(Member member, Posts posts);
}
