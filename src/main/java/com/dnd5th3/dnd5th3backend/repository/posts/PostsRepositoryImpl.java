package com.dnd5th3.dnd5th3backend.repository.posts;

import com.dnd5th3.dnd5th3backend.controller.dto.post.SortType;
import com.dnd5th3.dnd5th3backend.domain.member.Member;
import com.dnd5th3.dnd5th3backend.domain.posts.Posts;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.EntityPath;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
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

    @Override
    public List<Posts> findPostsWithSortType(String sortType) {
        return query
                .selectFrom(posts)
                .join(posts.member)
                .fetchJoin()
                .where(eqSortType(sortType))
                .orderBy(specifier(sortType))
                .fetch();
    }

    private BooleanExpression eqSortType(String sortType) {
        if (SortType.RANK_COUNT.getValue().equals(sortType)) {
            return posts.isPostsEnd.eq(false);
        } else if (SortType.ALMOST_DONE.getValue().equals(sortType)) {
            return posts.isVoted.eq(false);
        } else if (SortType.ALREADY_DONE.getValue().equals(sortType)) {
            return posts.isVoted.eq(true);
        }

        return null;
    }

    private OrderSpecifier specifier(String sorted) {
        if (SortType.RANK_COUNT.getValue().equals(sorted)) {
            return posts.rankCount.desc();
        } else if (SortType.CREATED_DATE.getValue().equals(sorted)) {
            return posts.createdDate.desc();
        } else if (SortType.ALMOST_DONE.getValue().equals(sorted)) {
            return posts.voteDeadline.asc();
        } else if (SortType.ALREADY_DONE.equals(sorted)) {
            return posts.voteDeadline.desc();
        }

        /**
         * 조건이 없을시 생성된 시간으로 정렬
         */
        return posts.createdDate.desc();
    }
}
