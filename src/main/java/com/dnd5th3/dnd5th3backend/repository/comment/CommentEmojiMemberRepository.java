package com.dnd5th3.dnd5th3backend.repository.comment;

import com.dnd5th3.dnd5th3backend.domain.comment.CommentEmojiMember;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentEmojiMemberRepository extends JpaRepository<CommentEmojiMember,Long> {

    void deleteByCommentEmojiIdAndMemberId(long commentEmojiId, long memberId);
}
