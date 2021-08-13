package com.dnd5th3.dnd5th3backend.repository.comment;


import com.dnd5th3.dnd5th3backend.domain.comment.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment,Long>,CommentRepositoryCustom {

    int countByGroupNoAndCommentLayer(long groupNo,int commentLayer);
}
