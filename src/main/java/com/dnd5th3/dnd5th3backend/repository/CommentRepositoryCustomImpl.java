package com.dnd5th3.dnd5th3backend.repository;

import com.dnd5th3.dnd5th3backend.domain.comment.Comment;
import com.querydsl.core.QueryResults;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import static com.dnd5th3.dnd5th3backend.domain.comment.QComment.comment;

import java.util.List;

@RequiredArgsConstructor
public class CommentRepositoryCustomImpl implements CommentRepositoryCustom {

    private final JPAQueryFactory query;

    @Override
    public Page<Comment> getAllCommentList(long postId, Pageable pageable){
        QueryResults<Comment> results = query.selectFrom(comment)
                .where(comment.posts.id.eq(postId))
                .orderBy(comment.groupNo.desc(), comment.commentOrder.asc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();

        List<Comment> commentList = results.getResults();
        long total = results.getTotal();

        return new PageImpl<>(commentList,pageable,total);
    }
}
