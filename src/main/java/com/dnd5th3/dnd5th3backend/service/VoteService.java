package com.dnd5th3.dnd5th3backend.service;

import com.dnd5th3.dnd5th3backend.domain.member.Member;
import com.dnd5th3.dnd5th3backend.domain.posts.Posts;
import com.dnd5th3.dnd5th3backend.domain.vote.Vote;
import com.dnd5th3.dnd5th3backend.repository.VoteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional
@Service
public class VoteService {

    private final VoteRepository voteRepository;

    public Vote saveVote(Member member, Posts posts) {
        Vote newVote = Vote.builder()
                .member(member)
                .posts(posts)
                .build();

        return voteRepository.save(newVote);
    }

    public Boolean isCurrentMemberVoted(Member member, Posts posts) {

        return voteRepository.currentMemberVoted(member, posts);
    }
}
