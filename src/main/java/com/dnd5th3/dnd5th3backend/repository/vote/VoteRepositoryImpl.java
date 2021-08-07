package com.dnd5th3.dnd5th3backend.repository.vote;

import com.dnd5th3.dnd5th3backend.domain.member.Member;
import com.dnd5th3.dnd5th3backend.domain.posts.Posts;
import com.dnd5th3.dnd5th3backend.domain.vote.Vote;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import static com.dnd5th3.dnd5th3backend.domain.vote.QVote.vote;

@RequiredArgsConstructor
public class VoteRepositoryImpl implements VoteRepositoryCustom{

    private final JPAQueryFactory query;

    @Override
    public Vote findByMemberAndPosts(Member member, Posts posts) {
        return query
                .select(vote)
                .from(vote)
                .where(vote.member.eq(member), vote.posts.eq(posts))
                .fetchOne();
    }
}
