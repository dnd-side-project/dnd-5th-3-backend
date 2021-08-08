package com.dnd5th3.dnd5th3backend.repository;

import com.dnd5th3.dnd5th3backend.domain.comment.CommentEmojiMember;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentEmojiMemberRepository extends JpaRepository<CommentEmojiMember,Long> {
}
