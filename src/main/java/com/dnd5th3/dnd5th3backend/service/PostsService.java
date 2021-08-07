package com.dnd5th3.dnd5th3backend.service;

import com.dnd5th3.dnd5th3backend.domain.member.Member;
import com.dnd5th3.dnd5th3backend.domain.posts.Posts;
import com.dnd5th3.dnd5th3backend.exception.PostNotFoundException;
import com.dnd5th3.dnd5th3backend.repository.PostsRepository;
import lombok.RequiredArgsConstructor;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Transactional
@Service
public class PostsService {

    private final PostsRepository postsRepository;

    public Posts savePost(Member member, String title, String productName, String content, String productImageUrl) {
        Posts newPosts = Posts.builder()
                .member(member)
                .title(title)
                .productName(productName)
                .content(content)
                .productImageUrl(productImageUrl)
                .isVoted(false)
                .permitCount(0)
                .rejectCount(0)
                .rankCount(0)
                .isDeleted(false)
                .voteDeadline(LocalDateTime.now().plusDays(1L))
                .build();
        return postsRepository.save(newPosts);
    }

    public Posts findPostById(Long id) {
        Posts foundPost = postsRepository.findById(id).orElseThrow(() -> new PostNotFoundException("해당 Id의 게시글이 존재하지 않습니다."));
        //프록시 객체 초기화
        Hibernate.initialize(foundPost.getMember());
        //투표 종료 여부
        if (LocalDateTime.now().isAfter(foundPost.getVoteDeadline())) {
            foundPost.makeVotedStatusTrue();
        }
        //조회수 증가
        foundPost.increaseRankCount();

        return foundPost;
    }

    public Posts updatePost(Long id, String title, String productName, String content, String productImageUrl) {
        Posts foundPost = postsRepository.findById(id).orElseThrow(() -> new PostNotFoundException("해당 Id의 게시글이 존재하지 않습니다."));
        //프록시 객체 초기화
        Hibernate.initialize(foundPost.getMember());
        foundPost.update(title, productName, content, productImageUrl);

        return foundPost;
    }

    public void deletePost(Long id) {
        Posts foundPost = postsRepository.findById(id).orElseThrow(() -> new PostNotFoundException("해당 Id의 게시글이 존재하지 않습니다."));
        foundPost.makeDeletedStatusTrue();
        postsRepository.delete(foundPost);
    }

    public List<Posts> findAllPosts(String sorted, int offset) {
        List<Posts> allPosts = postsRepository.findAll();
        //프록시 객체 초기화, 투표 종료 여부 초기화
        allPosts.stream().forEach(p -> {
            Hibernate.initialize(p.getMember());
            if (p.getIsVoted() == false && LocalDateTime.now().isAfter(p.getVoteDeadline())) {
                p.makeVotedStatusTrue();
            }
        });

        if (sorted.equals("rank-count")) {
            List<Posts> allPostsOrderByRankCount = postsRepository.findPostsOrderByRankCount(offset);
            return allPostsOrderByRankCount;
        }
        if (sorted.equals("created-date")) {
            List<Posts> allPostsOrderByCreatedDate = postsRepository.findPostsOrderByCreatedDate(offset);
            return allPostsOrderByCreatedDate;
        }
        if (sorted.equals("already-done")) {
            List<Posts> allPostsOrderByAlreadyDone = postsRepository.findPostsOrderByAlreadyDone(offset);
            return allPostsOrderByAlreadyDone;
        }
        if (sorted.equals("almost-done")) {
            List<Posts> allPostsOrderByAlmostDone = postsRepository.findPostsOrderByAlmostDone(offset);
            return allPostsOrderByAlmostDone;
        }

        return allPosts;
    }
}
