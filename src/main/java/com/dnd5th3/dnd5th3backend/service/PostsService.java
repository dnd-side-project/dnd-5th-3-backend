package com.dnd5th3.dnd5th3backend.service;

import com.dnd5th3.dnd5th3backend.domain.member.Member;
import com.dnd5th3.dnd5th3backend.domain.posts.Posts;
import com.dnd5th3.dnd5th3backend.exception.PostNotFoundException;
import com.dnd5th3.dnd5th3backend.repository.PostsRepository;
import lombok.RequiredArgsConstructor;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class PostsService {

    private final PostsRepository postsRepository;

    @Transactional
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
                .viewCount(0)
                .isDeleted(false)
                .build();
        return postsRepository.save(newPosts);
    }

    public Posts findPostById(Long id) {
        Posts foundPost = postsRepository.findById(id).orElseThrow(() -> new PostNotFoundException("해당 Id의 게시글이 존재하지 않습니다."));
        //프록시 객체 초기화
        Hibernate.initialize(foundPost.getMember());
        return foundPost;
    }

    @Transactional
    public Posts updatePost(Long id, String title, String productName, String content, String productImageUrl) {
        Posts foundPost = postsRepository.findById(id).orElseThrow(() -> new PostNotFoundException("해당 Id의 게시글이 존재하지 않습니다."));
        //프록시 객체 초기화
        Hibernate.initialize(foundPost.getMember());
        foundPost.update(title, productName, content, productImageUrl);

        return foundPost;
    }

    @Transactional
    public void deletePost(Long id) {
        Posts foundPost = postsRepository.findById(id).orElseThrow(() -> new PostNotFoundException("해당 Id의 게시글이 존재하지 않습니다."));
        postsRepository.delete(foundPost);
    }
}
