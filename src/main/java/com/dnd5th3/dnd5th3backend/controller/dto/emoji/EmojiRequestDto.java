package com.dnd5th3.dnd5th3backend.controller.dto.emoji;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class EmojiRequestDto {
    private final Long commentId;
    private final Long emojiId;
    private final Long commentEmojiId;
    private final Boolean isChecked;
}
