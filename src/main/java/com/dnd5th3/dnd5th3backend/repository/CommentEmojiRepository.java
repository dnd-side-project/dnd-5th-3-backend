package com.dnd5th3.dnd5th3backend.repository;

import com.dnd5th3.dnd5th3backend.domain.comment.CommentEmoji;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentEmojiRepository extends JpaRepository<CommentEmoji,Long> {

}
