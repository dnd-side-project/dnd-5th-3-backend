package com.dnd5th3.dnd5th3backend.controller.dto.comment;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CommentRequestDto {
    private final Long postId;
    private final Long commentId;
    private final Integer commentLayer;
    private final String content;
}