package com.dnd5th3.dnd5th3backend.repository.posts;

import com.dnd5th3.dnd5th3backend.domain.member.Member;
import com.dnd5th3.dnd5th3backend.domain.posts.Posts;

import java.util.List;

public interface PostsRepositoryCustom {

    List<Posts> findPostsOrderByRankCount();
    List<Posts> findPostsOrderByCreatedDate();
    List<Posts> findPostsOrderByAlreadyDone();
    List<Posts> findPostsOrderByAlmostDone();
    List<Posts> findPostsTop50Ranked();
    List<Posts> findPostsByMemberOrderByCreatedDate(Member member);
    List<Posts> findPostsWithSortType(String sortType);
}
