package com.dnd5th3.dnd5th3backend.service;

import com.dnd5th3.dnd5th3backend.controller.dto.post.UpdateRequestDto;
import com.dnd5th3.dnd5th3backend.domain.member.Member;
import com.dnd5th3.dnd5th3backend.domain.member.Role;
import com.dnd5th3.dnd5th3backend.domain.posts.Posts;
import com.dnd5th3.dnd5th3backend.repository.PostsRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PostsServiceTest {

    @Mock
    private PostsRepository postsRepository;

    @InjectMocks
    private PostsService postsService;

    private Member member;

    @BeforeEach
    public void setUp() {
        member = Member.builder().email("test@gmail.com").password("1234").role(Role.ROLE_USER).name("닉네임").build();
    }

    @DisplayName("post 저장 테스트")
    @Test
    public void savePostTest() throws Exception{
        //given
        Posts newPosts = Posts.builder()
                .member(member)
                .title("test")
                .productName("testProduct")
                .content("test content")
                .productImageUrl("test.jpg")
                .isVoted(false)
                .permitCount(0)
                .rejectCount(0)
                .viewCount(0)
                .isDeleted(false)
                .build();
        when(postsRepository.save(any(Posts.class))).thenReturn(newPosts);

        //when
        Posts savedPosts = postsService.savePost(newPosts.getMember(), newPosts.getTitle(), newPosts.getProductName(), newPosts.getContent(), newPosts.getProductImageUrl());

        //then
        Assertions.assertEquals(savedPosts.getId(), newPosts.getId());
        Assertions.assertEquals(savedPosts.getMember().getId(), newPosts.getMember().getId());
    }

    @DisplayName("post 상세조회 테스트")
    @Test
    public void findPostByIdTest() throws Exception{
        //given
        Posts post = Posts.builder().id(1L).member(member).title("test").productName("testProduct").content("test content").build();
        when(postsRepository.findById(1L)).thenReturn(Optional.of(post));

        //when
        Posts foundPost = postsService.findPostById(1L);

        //then
        Assertions.assertEquals(post.getMember().getName(), foundPost.getMember().getName());
        Assertions.assertEquals(post.getTitle(), foundPost.getTitle());
    }

    @DisplayName("post 수정 테스트")
    @Test
    public void updatePostTest() throws Exception {
        //given
        Posts post = Posts.builder().id(1L).member(member).title("test").productName("testProduct").content("test content").build();
        UpdateRequestDto requestDto = UpdateRequestDto.builder().title("update").productName("update product").content("update contest").productImageUrl("update.jpg").build();
        when(postsRepository.findById(1L)).thenReturn(Optional.of(post));

        //when
        Posts updatedPost = postsService.updatePost(post.getId(), requestDto.getTitle(), requestDto.getProductName(), requestDto.getContent(), requestDto.getProductImageUrl());

        //then
        Assertions.assertEquals(updatedPost.getTitle(), requestDto.getTitle());
        Assertions.assertEquals(updatedPost.getContent(), requestDto.getContent());
        Assertions.assertEquals(updatedPost.getProductImageUrl(), requestDto.getProductImageUrl());
    }

    @DisplayName("post 삭제 테스트")
    @Test
    public void deletePostTest() throws Exception {
        //given
        Posts post = Posts.builder().id(1L).member(member).title("test").productName("testProduct").content("test content").build();
        when(postsRepository.findById(1L)).thenReturn(Optional.of(post));

        //when
        postsService.deletePost(post.getId());

        //then
        verify(postsRepository, times(1)).delete(eq(post));
    }

}