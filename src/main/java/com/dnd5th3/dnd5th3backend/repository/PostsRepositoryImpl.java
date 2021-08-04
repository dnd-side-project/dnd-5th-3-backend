package com.dnd5th3.dnd5th3backend.repository;

import com.dnd5th3.dnd5th3backend.domain.posts.Posts;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.dnd5th3.dnd5th3backend.domain.member.QMember.member;
import static com.dnd5th3.dnd5th3backend.domain.posts.QPosts.posts;

@RequiredArgsConstructor
public class PostsRepositoryImpl implements PostsRepositoryCustom{

    private final JPAQueryFactory query;

    @Override
    public List<Posts> findPostsOrderByViewCount(int offset) {

        return query
                .select(posts)
                .from(posts)
                .orderBy(posts.viewCount.desc())
                .offset(offset)
                .limit(10)
                .fetch();
    }

    @Override
    public List<Posts> findPostsOrderByCreatedDate(int offset) {

        return query
                .select(posts)
                .from(posts)
                .orderBy(posts.createdDate.desc())
                .offset(offset)
                .limit(10)
                .fetch();
    }

    @Override
    public List<Posts> findPostsOrderByAlreadyDone(int offset) {

        return query
                .select(posts)
                .from(posts)
                .where(posts.isVoted.eq(true))
                .orderBy(posts.voteDeadline.asc())
                .offset(offset)
                .limit(10)
                .fetch();
    }

    @Override
    public List<Posts> findPostsOrderByAlmostDone(int offset) {
        return query
                .select(posts)
                .from(posts)
                .where(posts.isVoted.eq(false))
                .orderBy(posts.voteDeadline.asc())
                .offset(offset)
                .limit(10)
                .fetch();
    }

}
