package com.dnd5th3.dnd5th3backend.service;

import com.dnd5th3.dnd5th3backend.controller.dto.emoji.EmojiRequestDto;
import com.dnd5th3.dnd5th3backend.domain.comment.Comment;
import com.dnd5th3.dnd5th3backend.domain.comment.CommentEmoji;
import com.dnd5th3.dnd5th3backend.domain.comment.CommentEmojiMember;
import com.dnd5th3.dnd5th3backend.domain.emoji.Emoji;
import com.dnd5th3.dnd5th3backend.domain.member.Member;
import com.dnd5th3.dnd5th3backend.repository.comment.CommentEmojiMemberRepository;
import com.dnd5th3.dnd5th3backend.repository.comment.CommentEmojiRepository;
import com.dnd5th3.dnd5th3backend.repository.comment.CommentRepository;
import com.dnd5th3.dnd5th3backend.repository.emoji.EmojiRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional
@Service
public class EmojiService {

    private final EmojiRepository emojiRepository;
    private final CommentRepository commentRepository;
    private final CommentEmojiRepository commentEmojiRepository;
    private final CommentEmojiMemberRepository commentEmojiMemberRepository;
    private static final int DEFAULT = 1;

    public long saveEmoji(String detail){
        Emoji emoji = emojiRepository.save(Emoji.create(detail));
        return emoji.getId();
    }

    public CommentEmoji saveCommentEmoji(EmojiRequestDto emojiRequestDto, Member member){

        Emoji emoji = emojiRepository.findById(emojiRequestDto.getEmojiId()).orElseThrow();
        Comment comment = commentRepository.findById(emojiRequestDto.getCommentId()).orElseThrow();

        CommentEmoji commentEmoji = commentEmojiRepository.save(CommentEmoji.builder()
                .comment(comment)
                .emoji(emoji)
                .commentEmojiCount(DEFAULT)
                .build());

        commentEmojiMemberRepository.save(CommentEmojiMember.builder()
                .member(member)
                .commentEmoji(commentEmoji)
                .build());

        return commentEmoji;
    }

    @Transactional
    public CommentEmoji updateCountCommentEmoji(EmojiRequestDto emojiRequestDto,Member member){

        CommentEmoji commentEmoji = commentEmojiRepository.findById(emojiRequestDto.getCommentEmojiId()).orElseThrow();

        if(Boolean.FALSE.equals(emojiRequestDto.getIsChecked())){
            commentEmojiMemberRepository.deleteByCommentEmojiIdAndMemberId(commentEmoji.getId(),member.getId());
        }else {
            commentEmojiMemberRepository.save(CommentEmojiMember.builder()
                    .commentEmoji(commentEmoji)
                    .member(member)
                    .build());
        }

        boolean isNeedToDelete = commentEmoji.update(emojiRequestDto.getIsChecked());

        if(isNeedToDelete){
            commentEmojiRepository.delete(commentEmoji);
        }

        return commentEmoji;
    }
}
