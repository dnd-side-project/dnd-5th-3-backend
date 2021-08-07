package com.dnd5th3.dnd5th3backend.repository;

import com.dnd5th3.dnd5th3backend.config.QuerydslConfig;
import com.dnd5th3.dnd5th3backend.domain.member.Member;
import com.dnd5th3.dnd5th3backend.domain.member.Role;
import com.dnd5th3.dnd5th3backend.domain.posts.Posts;
import com.dnd5th3.dnd5th3backend.domain.vote.Vote;
import com.dnd5th3.dnd5th3backend.domain.vote.VoteType;
import com.dnd5th3.dnd5th3backend.repository.member.MemberRepository;
import com.dnd5th3.dnd5th3backend.repository.posts.PostsRepository;
import com.dnd5th3.dnd5th3backend.repository.vote.VoteRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;

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

    @DisplayName("member와 post로 조회 테스트")
    @Test
    public void findByMemberAndPostsTest() throws Exception {
        Member member = Member.builder()
                .id(1L)
                .email("test@gmail.com")
                .password("1234")
                .role(Role.ROLE_USER)
                .name("닉네임").
                build();
        Posts posts = Posts.builder()
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

        Vote vote = Vote.builder()
                .id(1L)
                .member(member)
                .posts(posts)
                .result(VoteType.NO_RESULT)
                .build();
        voteRepository.save(vote);

        Vote byMemberAndPosts = voteRepository.findByMemberAndPosts(member, posts);

        Assertions.assertEquals(byMemberAndPosts.getId(), vote.getId());
        Assertions.assertEquals(byMemberAndPosts.getMember().getId(), vote.getMember().getId());
        Assertions.assertEquals(byMemberAndPosts.getPosts().getId(), vote.getPosts().getId());
    }
}