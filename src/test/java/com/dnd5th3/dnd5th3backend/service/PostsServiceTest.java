package com.dnd5th3.dnd5th3backend.service;

import com.dnd5th3.dnd5th3backend.controller.dto.post.AllPostResponseDto;
import com.dnd5th3.dnd5th3backend.controller.dto.post.SortType;
import com.dnd5th3.dnd5th3backend.domain.member.Member;
import com.dnd5th3.dnd5th3backend.domain.member.Role;
import com.dnd5th3.dnd5th3backend.domain.posts.Posts;
import com.dnd5th3.dnd5th3backend.repository.posts.PostsRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PostsServiceTest {

    @Mock
    private PostsRepository postsRepository;

    @InjectMocks
    private PostsService postsService;

    private Member member;
    private Posts post;

    @BeforeEach
    void setUp() {
        member = Member.builder().email("test@gmail.com").password("1234").role(Role.ROLE_USER).name("닉네임").build();
        post = Posts.builder()
                .id(1L)
                .member(member)
                .title("test")
                .content("test content")
                .productImageUrl("test.jpg")
                .isVoted(false)
                .isPostsEnd(false)
                .permitCount(0)
                .rejectCount(0)
                .rankCount(0)
                .voteDeadline(LocalDateTime.now().plusDays(1L))
                .postsDeadline(LocalDateTime.now().plusDays(7L))
                .build();
    }

    @DisplayName("post 저장 테스트")
    @Test
    void savePostTest() throws Exception{
        //given
        when(postsRepository.save(any(Posts.class))).thenReturn(post);

        //when
        Posts savedPosts = postsService.savePost(post.getMember(), post.getTitle(), post.getContent(), post.getProductImageUrl());

        //then
        assertEquals(savedPosts.getId(), post.getId());
        assertEquals(savedPosts.getMember().getId(), post.getMember().getId());
    }

    @DisplayName("post 상세조회 테스트")
    @Test
    void findPostByIdTest() throws Exception{
        //given
        when(postsRepository.findById(1L)).thenReturn(Optional.of(post));

        //when
        Posts foundPost = postsService.findPostById(1L);

        //then
        assertEquals(post.getMember().getName(), foundPost.getMember().getName());
        assertEquals(post.getTitle(), foundPost.getTitle());
        assertEquals(post.getRankCount(), 1);
        assertEquals(false, post.getIsVoted());
        assertEquals(false, post.getIsPostsEnd());
    }

    @DisplayName("투표 종료 여부, 메인페이지 게시 조건 종료 여부 검사 테스트")
    @Test
    void updateVoteStatusAndPostStatus() throws Exception {
        //given
        Posts voteEnd = Posts.builder().rankCount(0).isVoted(false).voteDeadline(LocalDateTime.now().minusDays(1L)).build();
        Posts postEnd = Posts.builder().rankCount(0).isPostsEnd(false).postsDeadline(LocalDateTime.now().minusDays(1L)).build();
        given(postsRepository.findById(2L)).willReturn(Optional.of(voteEnd));
        given(postsRepository.findById(3L)).willReturn(Optional.of(postEnd));

        //when
        Posts voteEndPost = postsService.findPostById(2L);
        Posts postEndPost = postsService.findPostById(3L);

        //then
        assertEquals(true, voteEndPost.getIsVoted());
        assertEquals(true, postEndPost.getIsPostsEnd());
    }

    @DisplayName("post 수정 테스트")
    @Test
    void updatePostTest() throws Exception {
        //given
        String title = "update";
        String content = "update content";
        String productImageUrl = "update.jpg";
        when(postsRepository.findById(1L)).thenReturn(Optional.of(post));

        //when
        Posts updatedPost = postsService.updatePost(post.getId(), title, content, productImageUrl);

        //then
        assertEquals(updatedPost.getTitle(), title);
        assertEquals(updatedPost.getContent(), content);
        assertEquals(updatedPost.getProductImageUrl(), productImageUrl);
    }

    @DisplayName("post 삭제 테스트")
    @Test
    void deletePostTest() throws Exception {
        //given
        when(postsRepository.findById(1L)).thenReturn(Optional.of(post));

        //when
        postsService.deletePost(post.getId(), member);

        //then
        verify(postsRepository, times(1)).delete(eq(post));
    }

    @DisplayName("정렬된 전체 게시물 조회 테스트")
    @Test
    void findAllPostsWithSortType() throws Exception {
        //given
        List<Posts> postsList = new ArrayList<>();
        postsList.add(post);
        given(postsRepository.findPostsWithSortType(SortType.RANK_COUNT.getValue())).willReturn(postsList);

        //when
        AllPostResponseDto responseDto = postsService.findAllPostsWithSortType(SortType.RANK_COUNT.getValue());

        //then
        assertEquals(responseDto.getListDtos().get(0).getId(), post.getId());
    }
}