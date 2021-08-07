package com.dnd5th3.dnd5th3backend.service;

import com.dnd5th3.dnd5th3backend.domain.member.Member;
import com.dnd5th3.dnd5th3backend.domain.member.Role;
import com.dnd5th3.dnd5th3backend.domain.posts.Posts;
import com.dnd5th3.dnd5th3backend.domain.vote.Vote;
import com.dnd5th3.dnd5th3backend.domain.vote.VoteType;
import com.dnd5th3.dnd5th3backend.repository.posts.PostsRepository;
import com.dnd5th3.dnd5th3backend.repository.vote.VoteRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class VoteServiceTest {

    @Mock
    private VoteRepository voteRepository;

    @Mock
    private PostsRepository postsRepository;

    @InjectMocks
    private VoteService voteService;

    private Member member;
    private Posts posts;
    private Vote vote;

    @BeforeEach
    public void setUp() {
        member = Member.builder()
                .id(1L)
                .email("test@gmail.com")
                .password("1234")
                .role(Role.ROLE_USER)
                .name("닉네임").
                build();
        posts = Posts.builder()
                .id(1L)
                .member(member)
                .title("test")
                .productName("test product")
                .content("test content")
                .productImageUrl("test.jpg")
                .isVoted(false)
                .permitCount(0)
                .rejectCount(0)
                .rankCount(0)
                .isDeleted(false)
                .voteDeadline(LocalDateTime.now().plusDays(1L))
                .build();
        vote = Vote.builder()
                .member(member)
                .posts(posts)
                .result(VoteType.NO_RESULT)
                .build();
    }

    @DisplayName("vote 저장 테스트")
    @Test
    public void saveVoteTest() throws Exception {
        //given
        when(postsRepository.findById(1L)).thenReturn(Optional.of(posts));
        when(voteRepository.save(any(Vote.class))).thenReturn(vote);

        //when
        Vote savedVote = voteService.saveVote(member, posts, VoteType.NO_RESULT);

        //then
        Assertions.assertEquals(savedVote.getId(), vote.getId());
        Assertions.assertEquals(savedVote.getMember(), vote.getMember());
        Assertions.assertEquals(savedVote.getPosts(), vote.getPosts());
    }

    @DisplayName("투표 결과 조회 테스트")
    @Test
    public void getVoteResultTest() throws Exception {
        //given
        when(voteRepository.findByMemberAndPosts(member, posts)).thenReturn(vote);

        //when
        Vote voteResult = voteService.getVoteResult(member, posts);

        //then
        Assertions.assertEquals(voteResult.getResult(), vote.getResult());
    }
}