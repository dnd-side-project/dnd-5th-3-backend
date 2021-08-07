package com.dnd5th3.dnd5th3backend.repository;

import com.dnd5th3.dnd5th3backend.config.QuerydslConfig;
import com.dnd5th3.dnd5th3backend.domain.member.Member;
import com.dnd5th3.dnd5th3backend.domain.member.Role;
import com.dnd5th3.dnd5th3backend.domain.posts.Posts;
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
import java.util.List;
import java.util.Optional;

@DataJpaTest
@ExtendWith(SpringExtension.class)
@Import(QuerydslConfig.class)
public class PostsRepositoryTest {

    @Autowired
    private PostsRepository postsRepository;

    @Autowired
    private MemberRepository memberRepository;

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
    }

    @DisplayName("id 테스트")
    @Test
    public void idStrategyTest() {
        Posts posts2 = Posts.builder()
                .id(2L)
                .member(member)
                .title("test2")
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
        postsRepository.save(posts);
        postsRepository.save(posts2);

        Assertions.assertEquals(1, Math.abs(posts2.getId() - posts.getId()));
    }

    @DisplayName("post 생성 테스트")
    @Test
    public void savePostTest() {
        Posts savedPosts = postsRepository.save(posts);

        Assertions.assertEquals(posts.getMember().getId(), savedPosts.getMember().getId());
        Assertions.assertEquals(posts.getTitle(), savedPosts.getTitle());
    }

    @DisplayName("findById 테스트")
    @Test
    public void findByIdTest() {
        postsRepository.save(posts);
        Optional<Posts> foundPost = postsRepository.findById(1L);

        foundPost.ifPresent(p -> {
            Assertions.assertEquals(p.getTitle(), posts.getTitle());
            Assertions.assertEquals(p.getContent(), posts.getContent());
        });
    }

    @DisplayName("post 삭제 테스트")
    @Test
    public void deletePostTest() throws Exception {
        postsRepository.save(posts);
        postsRepository.delete(posts);
        Optional<Posts> foundPost = postsRepository.findById(posts.getId());

        Assertions.assertEquals(foundPost, Optional.empty());
    }

    @DisplayName("인기순 조회 테스트")
    @Test
    public void findPostsOrderByViewCountTest() throws Exception {
        Posts posts2 = Posts.builder()
                .member(member)
                .title("test2")
                .productName("test product")
                .content("test content")
                .productImageUrl("test.jpg")
                .isVoted(false)
                .permitCount(0)
                .rejectCount(0)
                .rankCount(10)
                .isDeleted(false)
                .voteDeadline(LocalDateTime.now().plusDays(1L))
                .build();
        postsRepository.save(posts);
        postsRepository.save(posts2);

        List<Posts> orderByRankCount = postsRepository.findPostsOrderByRankCount(0);

        Assertions.assertEquals(orderByRankCount.get(0).getTitle(), posts2.getTitle());
        Assertions.assertEquals(orderByRankCount.get(1).getTitle(), posts.getTitle());
    }

    @DisplayName("최신순 조회 테스트")
    @Test
    public void findPostsOrderByCreatedDateTest() throws Exception {
        Posts posts2 = Posts.builder()
                .member(member)
                .title("test2")
                .productName("test product")
                .content("test content")
                .productImageUrl("test.jpg")
                .isVoted(false)
                .permitCount(0)
                .rejectCount(0)
                .rankCount(10)
                .isDeleted(false)
                .voteDeadline(LocalDateTime.now().plusDays(1L))
                .build();
        postsRepository.save(posts);
        postsRepository.save(posts2);

        List<Posts> orderByCreatedDate = postsRepository.findPostsOrderByCreatedDate(0);

        Assertions.assertEquals(orderByCreatedDate.get(0).getTitle(), posts2.getTitle());
        Assertions.assertEquals(orderByCreatedDate.get(1).getTitle(), posts.getTitle());
    }

    @DisplayName("최근마감순 조회 테스트")
    @Test
    public void findPostsOrderByAlreadyDoneTest() throws Exception {
        Posts posts2 = Posts.builder()
                .member(member)
                .title("test2")
                .productName("test product")
                .content("test content")
                .productImageUrl("test.jpg")
                .isVoted(true)
                .permitCount(0)
                .rejectCount(0)
                .rankCount(10)
                .isDeleted(false)
                .voteDeadline(LocalDateTime.now().plusDays(1L))
                .build();
        posts.makeVotedStatusTrue();
        postsRepository.save(posts);
        postsRepository.save(posts2);

        List<Posts> orderByAlreadyDone = postsRepository.findPostsOrderByAlreadyDone(0);

        Assertions.assertEquals(orderByAlreadyDone.get(0).getTitle(), posts.getTitle());
        Assertions.assertEquals(orderByAlreadyDone.get(1).getTitle(), posts2.getTitle());
    }

    @DisplayName("마감임박순 조회 테스트")
    @Test
    public void findPostsOrderByAlmostDoneTest() throws Exception {
        Posts posts2 = Posts.builder()
                .member(member)
                .title("test2")
                .productName("test product")
                .content("test content")
                .productImageUrl("test.jpg")
                .isVoted(false)
                .permitCount(0)
                .rejectCount(0)
                .rankCount(10)
                .isDeleted(false)
                .voteDeadline(LocalDateTime.now().plusDays(1L))
                .build();
        postsRepository.save(posts);
        postsRepository.save(posts2);

        List<Posts> orderByAlmostDone = postsRepository.findPostsOrderByAlmostDone(0);

        Assertions.assertEquals(orderByAlmostDone.get(0).getTitle(), posts.getTitle());
        Assertions.assertEquals(orderByAlmostDone.get(1).getTitle(), posts2.getTitle());
    }
}