package com.dnd5th3.dnd5th3backend.service;

import com.dnd5th3.dnd5th3backend.domain.member.Member;
import com.dnd5th3.dnd5th3backend.domain.member.Role;
import com.dnd5th3.dnd5th3backend.domain.posts.Posts;
import com.dnd5th3.dnd5th3backend.repository.PostRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PostsServiceTest {

    @Mock
    private PostRepository postRepository;

    @InjectMocks
    private PostService postService;

    private Member member;

    @BeforeEach
    public void setUp() {
        member = Member.builder().email("test@gmail.com").password("1234").role(Role.ROLE_USER).name("닉네임").build();
    }

    @DisplayName("post 저장 테스트")
    @Test
    public void savePostTest() throws Exception{
        //given
        Posts newPosts = Posts.builder().member(member).title("test").productName("testProduct").content("test content").build();
        when(postRepository.save(any(Posts.class))).thenReturn(newPosts);

        //when
        Posts savedPosts = postService.savePost(newPosts.getMember(), newPosts.getTitle(), newPosts.getProductName(), newPosts.getContent(), newPosts.getProductImageUrl());

        //then
        Assertions.assertEquals(savedPosts.getId(), newPosts.getId());
        Assertions.assertEquals(savedPosts.getMember().getId(), newPosts.getMember().getId());
    }
}