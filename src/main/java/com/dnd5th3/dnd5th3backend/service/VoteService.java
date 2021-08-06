package com.dnd5th3.dnd5th3backend.service;

import com.dnd5th3.dnd5th3backend.domain.member.Member;
import com.dnd5th3.dnd5th3backend.domain.posts.Posts;
import com.dnd5th3.dnd5th3backend.domain.vote.Vote;
import com.dnd5th3.dnd5th3backend.domain.vote.VoteType;
import com.dnd5th3.dnd5th3backend.exception.PostNotFoundException;
import com.dnd5th3.dnd5th3backend.repository.PostsRepository;
import com.dnd5th3.dnd5th3backend.repository.VoteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RequiredArgsConstructor
@Transactional
@Service
public class VoteService {

    private final VoteRepository voteRepository;
    private final PostsRepository postsRepository;

    public Vote saveVote(Member member, Posts posts, VoteType result) {
        //투표 생성
        Vote newVote = Vote.builder()
                .member(member)
                .posts(posts)
                .result(result)
                .build();
        //투표 카운트 증가
        Posts foundPost = postsRepository.findById(posts.getId()).orElseThrow(() -> new PostNotFoundException("해당 Id의 게시글이 존재하지 않습니다."));
        foundPost.increaseVoteCount(result);

        return voteRepository.save(newVote);
    }

    public Vote getVoteResult(Member member, Posts posts) {

        return voteRepository.findByMemberAndPosts(member, posts);
    }
}
