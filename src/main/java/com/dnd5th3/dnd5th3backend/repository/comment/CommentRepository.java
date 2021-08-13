package com.dnd5th3.dnd5th3backend.repository.comment;


import com.dnd5th3.dnd5th3backend.domain.comment.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment,Long>,CommentRepositoryCustom {

    int countByGroupNoAndCommentLayer(long groupNo,int commentLayer);

    @Query( " SELECT c FROM Comment c" +
            " WHERE c.posts.id = :postId AND c.groupNo = :groupNo" +
            " ORDER BY c.commentLayer, c.commentOrder")
    List<Comment> getCommentGroup(long postId,long groupNo);
}
