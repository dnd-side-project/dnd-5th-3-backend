package com.dnd5th3.dnd5th3backend.repository;


import com.dnd5th3.dnd5th3backend.domain.comment.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment,Long> {

    @Query("SELECT DISTINCT c FROM Comment c " +
            " LEFT JOIN FETCH c.commentEmoji" +
            " WHERE c.posts.id=:postId")
    List<Comment> fetchAllCommentList(long postId);

}
