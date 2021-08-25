package com.dnd5th3.dnd5th3backend.repository.vote;

import com.dnd5th3.dnd5th3backend.domain.member.Member;
import com.dnd5th3.dnd5th3backend.domain.posts.Posts;
import com.dnd5th3.dnd5th3backend.domain.vote.Vote;

import java.util.List;

public interface VoteRepositoryCustom {

    Vote findByMemberAndPosts(Member member, Posts posts);
    List<Vote> findVoteByMemberOrderByCreatedDate(Member member);
}
