package com.dnd5th3.dnd5th3backend.repository;

import com.dnd5th3.dnd5th3backend.config.QuerydslConfig;
import com.dnd5th3.dnd5th3backend.domain.member.Member;
import com.dnd5th3.dnd5th3backend.domain.member.Role;
import com.dnd5th3.dnd5th3backend.domain.posts.Posts;
import com.dnd5th3.dnd5th3backend.domain.vote.Vote;
import com.dnd5th3.dnd5th3backend.domain.vote.VoteType;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ExtendWith(SpringExtension.class)
@Import(QuerydslConfig.class)
public class VoteRepositoryTest {

    @Autowired
    private VoteRepository voteRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private PostsRepository postsRepository;

    private Vote vote;
    private Member member;
    private Posts posts;

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
        memberRepository.save(member);
        postsRepository.save(posts);

        vote = Vote.builder()
                .member(member)
                .posts(posts)
                .result(VoteType.NO_RESULT)
                .build();
        voteRepository.save(vote);
    }

    @DisplayName("id 테스트")
    @Test
    public void idStrategyTest() throws Exception {
        Vote vote2 = Vote.builder()
                .member(member)
                .posts(posts)
                .result(VoteType.NO_RESULT)
                .build();
        voteRepository.save(vote2);

        Assertions.assertEquals(1, Math.abs(vote2.getId() - vote.getId()));
    }

    @DisplayName("vote 생성 테스트")
    @Test
    public void saveVoteTest() {
        Vote savedVote = voteRepository.save(vote);

        Assertions.assertEquals(vote.getId(), savedVote.getId());
        Assertions.assertEquals(vote.getMember().getId(), savedVote.getMember().getId());
        Assertions.assertEquals(vote.getPosts().getId(), savedVote.getPosts().getId());
    }

    @DisplayName("member와 post로 조회 테스트")
    @Test
    public void findByMemberAndPostsTest() throws Exception {
        Vote byMemberAndPosts = voteRepository.findByMemberAndPosts(member, posts);

        Assertions.assertEquals(byMemberAndPosts.getMember().getId(), vote.getMember().getId());
        Assertions.assertEquals(byMemberAndPosts.getPosts().getId(), vote.getPosts().getId());
    }
}