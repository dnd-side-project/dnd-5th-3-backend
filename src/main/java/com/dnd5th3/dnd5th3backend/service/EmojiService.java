package com.dnd5th3.dnd5th3backend.service;

import com.dnd5th3.dnd5th3backend.controller.dto.emoji.EmojiRequestDto;
import com.dnd5th3.dnd5th3backend.domain.comment.Comment;
import com.dnd5th3.dnd5th3backend.domain.comment.CommentEmoji;
import com.dnd5th3.dnd5th3backend.domain.comment.CommentEmojiMember;
import com.dnd5th3.dnd5th3backend.domain.emoji.Emoji;
import com.dnd5th3.dnd5th3backend.domain.member.Member;
import com.dnd5th3.dnd5th3backend.repository.CommentEmojiMemberRepository;
import com.dnd5th3.dnd5th3backend.repository.CommentEmojiRepository;
import com.dnd5th3.dnd5th3backend.repository.CommentRepository;
import com.dnd5th3.dnd5th3backend.repository.EmojiRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
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
        Comment comment = commentRepository.findById(emojiRequestDto.getEmojiId()).orElseThrow();

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

        boolean isNeedToDelete = commentEmoji.update(emojiRequestDto.getIsChecked());

        if(isNeedToDelete){
            commentEmojiRepository.delete(commentEmoji);
        }

        if(Boolean.FALSE.equals(emojiRequestDto.getIsChecked())){
            commentEmojiMemberRepository.delete(CommentEmojiMember.builder()
                    .commentEmoji(commentEmoji)
                    .member(member)
                    .build());
        }

        return commentEmoji;
    }
}
