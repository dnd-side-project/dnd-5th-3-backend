package com.dnd5th3.dnd5th3backend.controller.dto.comment;

import com.dnd5th3.dnd5th3backend.domain.comment.Comment;
import com.dnd5th3.dnd5th3backend.domain.member.Member;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CommentResponseDto {

    private final String memberEmail;
    private final Long postId;
    private final Long commentId;
    private final Long groupNo;
    private final Integer commentLayer;
    private final Integer commentOrder;
    private final String content;

    public static CommentResponseDto of(Comment comment, Member member){
        return CommentResponseDto
                .builder()
                .memberEmail(member.getEmail())
                .commentId(comment.getId())
                .groupNo(comment.getGroupNo())
                .commentLayer(comment.getCommentLayer())
                .commentOrder(comment.getCommentOrder())
                .content(comment.getContent())
                .build();
    }
}
