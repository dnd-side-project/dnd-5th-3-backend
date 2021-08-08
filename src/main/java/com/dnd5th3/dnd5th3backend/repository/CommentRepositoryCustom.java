package com.dnd5th3.dnd5th3backend.repository;

import com.dnd5th3.dnd5th3backend.domain.comment.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface CommentRepositoryCustom {
   Page<Comment> getAllCommentList(long postId, Pageable pageable);
}
