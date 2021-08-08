package com.dnd5th3.dnd5th3backend.controller.dto.comment;

import lombok.*;

import java.util.List;


@Builder
@Getter
public class CommentListResponseDto {

    List<CommentDto> commentResponseList;

    @Setter
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CommentDto{

        private long id;
        private long groupNo;
        private int commentLayer;
        private int commentOrder;
        private String content;
        List<EmojiIDto> emojiList;
    }

    @Setter
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class EmojiIDto{
        private long emojiId;
        private long count;
        private boolean isChecked;
    }
}
