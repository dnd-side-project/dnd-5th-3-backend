package com.dnd5th3.dnd5th3backend.repository.posts;

import com.dnd5th3.dnd5th3backend.domain.member.Member;
import com.dnd5th3.dnd5th3backend.domain.posts.Posts;
import com.querydsl.core.types.EntityPath;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.dnd5th3.dnd5th3backend.domain.posts.QPosts.posts;

@RequiredArgsConstructor
public class PostsRepositoryImpl implements PostsRepositoryCustom{

    private final JPAQueryFactory query;

    @Override
    public List<Posts> findPostsOrderByRankCount() {

        return query
                .select(posts)
                .from(posts)
                .where(posts.isPostsEnd.eq(false))
                .orderBy(posts.rankCount.desc())
                .fetchAll()
                .fetch();
    }

    @Override
    public List<Posts> findPostsOrderByCreatedDate() {

        return query
                .select(posts)
                .from(posts)
                .orderBy(posts.createdDate.desc())
                .fetchAll()
                .fetch();
    }

    @Override
    public List<Posts> findPostsOrderByAlreadyDone() {
        return query
                .select(posts)
                .from(posts)
                .where(posts.isVoted.eq(true))
                .orderBy(posts.voteDeadline.desc())
                .fetchAll()
                .fetch();
    }

    @Override
    public List<Posts> findPostsOrderByAlmostDone() {
        return query
                .select(posts)
                .from(posts)
                .where(posts.isVoted.eq(false))
                .orderBy(posts.voteDeadline.asc())
                .fetchAll()
                .fetch();
    }

    @Override
    public List<Posts> findPostsTop50Ranked() {
        return query
                .select(posts)
                .from(posts)
                .where(posts.isPostsEnd.eq(false))
                .orderBy(posts.rankCount.desc())
                .offset(0)
                .limit(50)
                .fetch();
    }

    @Override
    public List<Posts> findPostsByMemberOrderByCreatedDate(Member member) {
        return query
                .select(posts)
                .from(posts)
                .where(posts.member.eq(member))
                .orderBy(posts.createdDate.desc())
                .fetchAll()
                .fetch();
    }
}
