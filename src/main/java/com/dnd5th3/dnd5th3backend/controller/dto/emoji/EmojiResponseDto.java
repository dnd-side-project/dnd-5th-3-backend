package com.dnd5th3.dnd5th3backend.controller.dto.emoji;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class EmojiResponseDto {
    private final Long commentEmojiId;
    private final Integer commentEmojiCount;
}
