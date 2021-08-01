package com.dnd5th3.dnd5th3backend.repository;

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
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

@DataJpaTest
@ExtendWith(SpringExtension.class)
public class PostsRepositoryTest {

    @Autowired
    private PostsRepository postsRepository;

    private Member member;
    private Posts posts;

    @BeforeEach
    public void setUp() {
        member = Member.builder().email("test@gmail.com").password("1234").role(Role.ROLE_USER).name("닉네임").build();
        posts = Posts.builder().member(member).title("test").productName("testProduct").content("test content").build();
    }

    @DisplayName("id 테스트")
    @Test
    public void idStrategyTest() {
        Posts posts2 = Posts.builder().member(member).title("test2").productName("testProduct2").content("test content2").build();
        postsRepository.save(posts);
        postsRepository.save(posts2);

        Assertions.assertEquals(1, Math.abs(posts2.getId() - posts.getId()));
    }

    @DisplayName("post 생성 테스트")
    @Test
    public void savePostTest() {
        Posts posts = Posts.builder().member(member).title("test").productName("testProduct").content("test content").build();
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
}