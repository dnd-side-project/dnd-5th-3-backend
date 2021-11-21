package com.dnd5th3.dnd5th3backend.service;

import com.dnd5th3.dnd5th3backend.controller.dto.mypage.InfoResponseDto;
import com.dnd5th3.dnd5th3backend.controller.dto.mypage.SortType;
import com.dnd5th3.dnd5th3backend.domain.member.Member;
import com.dnd5th3.dnd5th3backend.domain.posts.Posts;
import com.dnd5th3.dnd5th3backend.repository.posts.PostsRepository;
import com.dnd5th3.dnd5th3backend.repository.vote.VoteRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class MyPageServiceTest {

    @Mock
    private PostsRepository postsRepository;

    @Mock
    private VoteRepository voteRepository;

    @InjectMocks
    private MyPageService myPageService;

    private Member member;
    private Posts posts;
    private List<Posts> postsList;

    @BeforeEach
    void setup() {
        member = Member.builder().id(1L).name("name").email("email").build();
        posts = Posts.builder().id(1L).member(member).permitCount(0).rejectCount(0).build();
        postsList = new ArrayList<>();
        postsList.add(0, posts);
    }

    @DisplayName("사용자 이름, 이메일, 정렬된 게시글들을 보여준다.")
    @Test
    void findMemberInfoWithSortTypeTest() {
        //given
        given(postsRepository.findPostsByMemberOrderByCreatedDate(member)).willReturn(postsList);

        //when
        InfoResponseDto responseDto = myPageService.findMemberInfoWithSortType(member, SortType.WRITTEN.getValue());

        //then
        assertThat("name").isEqualTo(responseDto.getName());
        assertThat("email").isEqualTo(responseDto.getEmail());
        assertThat(1L).isEqualTo(responseDto.getPostsList().get(0).getId());
    }
}
