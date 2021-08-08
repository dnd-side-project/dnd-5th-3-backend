package com.dnd5th3.dnd5th3backend.controller.dto.comment;

import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentResponseDto {

    private Long commentId;
    private Long groupNo;
    private Integer commentLayer;
    private Integer commentOrder;
    private String content;
    private Boolean isDeleted;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;

}
