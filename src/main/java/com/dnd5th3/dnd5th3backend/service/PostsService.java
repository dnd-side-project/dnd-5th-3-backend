package com.dnd5th3.dnd5th3backend.service;

import com.dnd5th3.dnd5th3backend.domain.member.Member;
import com.dnd5th3.dnd5th3backend.domain.posts.Posts;
import com.dnd5th3.dnd5th3backend.repository.PostsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class PostsService {

    private final PostsRepository postsRepository;

    @Transactional
    public Posts savePost(Member member, String title, String productName, String content, String productImageUrl) {
        Posts newPosts = Posts.builder().member(member).title(title).productName(productName).content(content).productImageUrl(productImageUrl).build();
        return postsRepository.save(newPosts);
    }
}
