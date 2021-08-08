package com.dnd5th3.dnd5th3backend.controller.dto.comment;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;


@Builder
@Getter
public class CommentListResponseDto {

    private List<CommentDto> commentResponseList;
    private int pageNum;
    private int totalPage;
    private long totalCount;

    @Setter
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CommentDto{

        private long commentId;
        private long groupNo;
        private int commentLayer;
        private int commentOrder;
        private String content;
        private LocalDateTime createdDate;
        private LocalDateTime updatedDate;
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
