package com.dnd5th3.dnd5th3backend.repository;

import com.dnd5th3.dnd5th3backend.domain.posts.Posts;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.dnd5th3.dnd5th3backend.domain.posts.QPosts.posts;

@RequiredArgsConstructor
public class PostsRepositoryImpl implements PostsRepositoryCustom{

    private final JPAQueryFactory query;

    @Override
    public List<Posts> findPostsOrderByViewCount() {

        return query
                .select(posts)
                .from(posts)
                .orderBy(posts.viewCount.desc())
                .limit(10)
                .fetch();
    }

    @Override
    public List<Posts> findPostsOrderByCreatedDate() {

        return query
                .select(posts)
                .from(posts)
                .orderBy(posts.createdDate.desc())
                .limit(10)
                .fetch();
    }
}
