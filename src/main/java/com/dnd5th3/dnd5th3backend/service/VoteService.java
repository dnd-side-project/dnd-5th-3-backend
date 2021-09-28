package com.dnd5th3.dnd5th3backend.service;

import com.dnd5th3.dnd5th3backend.domain.member.Member;
import com.dnd5th3.dnd5th3backend.domain.posts.Posts;
import com.dnd5th3.dnd5th3backend.domain.vote.Vote;
import com.dnd5th3.dnd5th3backend.domain.vote.VoteType;
import com.dnd5th3.dnd5th3backend.exception.DuplicateVoteException;
import com.dnd5th3.dnd5th3backend.exception.PostNotFoundException;
import com.dnd5th3.dnd5th3backend.repository.posts.PostsRepository;
import com.dnd5th3.dnd5th3backend.repository.vote.VoteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional
@Service
public class VoteService {

    private final VoteRepository voteRepository;
    private final PostsRepository postsRepository;

    public Vote saveVote(Member member, Posts posts, VoteType result) {
        //투표 중복 검사 확인
        Vote duplicationCheck = voteRepository.findByMemberAndPosts(member, posts);
        if (duplicationCheck != null) {
            throw new DuplicateVoteException("이미 투표한 사용자입니다.");
        }
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
