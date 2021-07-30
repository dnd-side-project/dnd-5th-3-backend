package com.dnd5th3.dnd5th3backend.repository;

import com.dnd5th3.dnd5th3backend.domain.member.Member;
import com.dnd5th3.dnd5th3backend.domain.member.Role;
import com.dnd5th3.dnd5th3backend.domain.post.Post;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ExtendWith(SpringExtension.class)
public class PostRepositoryTest {

    @Autowired
    private PostRepository postRepository;

    private Member member;
    private Post post;

    @BeforeEach
    public void setUp() {
        member = Member.builder().email("test@gmail.com").password("1234").role(Role.ROLE_USER).name("닉네임").build();
        post = Post.builder().member(member).title("test").productName("testProduct").content("test content").build();
    }

    @DisplayName("id 테스트")
    @Test
    public void idStrategyTest() {
        Post post2 = Post.builder().member(member).title("test2").productName("testProduct2").content("test content2").build();
        postRepository.save(post);
        postRepository.save(post2);

        Assertions.assertEquals(1, Math.abs(post2.getId() - post.getId()));
    }

    @DisplayName("post 생성 테스트")
    @Test
    public void savePostTest() {
        Post post = Post.builder().member(member).title("test").productName("testProduct").content("test content").build();
        Post savedPost = postRepository.save(post);

        Assertions.assertEquals(post.getMember().getId(), savedPost.getMember().getId());
        Assertions.assertEquals(post.getTitle(), savedPost.getTitle());
    }

    @DisplayName("findById 테스트")
    @Test
    public void findByIdTest() {
        postRepository.save(post);
        Optional<Post> foundPost = postRepository.findById(1L);

        foundPost.ifPresent(p -> {
            Assertions.assertEquals(p.getTitle(), post.getTitle());
            Assertions.assertEquals(p.getContent(), post.getContent());
        });
    }
}