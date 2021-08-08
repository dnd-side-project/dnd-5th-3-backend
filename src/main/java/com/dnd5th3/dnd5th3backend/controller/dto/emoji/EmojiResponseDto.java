package com.dnd5th3.dnd5th3backend.controller.dto.emoji;


import com.dnd5th3.dnd5th3backend.domain.comment.CommentEmoji;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class EmojiResponseDto {
    private final Long commentEmojiId;
    private final Integer commentEmojiCount;

    public static EmojiResponseDto of(CommentEmoji commentEmoji){
        return EmojiResponseDto.builder()
                .commentEmojiId(commentEmoji.getId())
                .commentEmojiCount(commentEmoji.getCommentEmojiCount())
                .build();
    }

}
