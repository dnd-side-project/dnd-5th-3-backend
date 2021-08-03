package com.dnd5th3.dnd5th3backend.repository;

import com.dnd5th3.dnd5th3backend.domain.posts.Posts;

import java.util.List;

public interface PostsRepositoryCustom {

    List<Posts> findPostsOrderByViewCount();
    List<Posts> findPostsOrderByCreatedDate();
}
