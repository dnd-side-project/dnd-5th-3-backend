package com.dnd5th3.dnd5th3backend.repository.posts;

import com.dnd5th3.dnd5th3backend.domain.member.Member;
import com.dnd5th3.dnd5th3backend.domain.posts.Posts;

import java.util.List;
import java.util.Optional;

public interface PostsRepositoryCustom {

    Posts findPostsById(Long id);
    List<Posts> findPostsTop50Ranked();
    List<Posts> findPostsByMemberOrderByCreatedDate(Member member);
    List<Posts> findPostsWithSortType(String sortType);
}
