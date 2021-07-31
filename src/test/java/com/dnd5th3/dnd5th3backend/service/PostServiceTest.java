package com.dnd5th3.dnd5th3backend.service;

import com.dnd5th3.dnd5th3backend.domain.member.Member;
import com.dnd5th3.dnd5th3backend.domain.member.Role;
import com.dnd5th3.dnd5th3backend.domain.post.Post;
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
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PostServiceTest {

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
        Post newPost = Post.builder().member(member).title("test").productName("testProduct").content("test content").build();
        when(postRepository.save(any(Post.class))).thenReturn(newPost);

        //when
        Post savedPost = postService.savePost(newPost.getMember(), newPost.getTitle(), newPost.getProductName(), newPost.getContent(), newPost.getProductImageUrl());

        //then
        Assertions.assertEquals(savedPost.getId(), newPost.getId());
        Assertions.assertEquals(savedPost.getMember().getId(), newPost.getMember().getId());
    }
}