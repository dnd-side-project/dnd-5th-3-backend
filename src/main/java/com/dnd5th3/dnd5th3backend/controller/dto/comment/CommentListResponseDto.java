package com.dnd5th3.dnd5th3backend.controller.dto.comment;

import com.dnd5th3.dnd5th3backend.domain.vote.VoteType;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;


@Builder
@Getter
public class CommentListResponseDto {

    private List<CommentDto> commentList;
    private int pageNum;
    private int totalPage;
    private long totalCount;

    @Setter
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CommentDto{

        private long commentId;
        private long memberId;
        private String email;
        private String writerName;
        private long groupNo;
        private int commentLayer;
        private int commentOrder;
        private String content;
        private VoteType voteType;
        private int replyCount;
        private boolean isDeleted;
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        private LocalDateTime createdDate;
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        private LocalDateTime updatedDate;
        List<EmojiIDto> emojiList;
    }

    @Setter
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class EmojiIDto{
        private long emojiId;
        private long emojiCount;
        private boolean isChecked;
    }
}
