package com.dnd5th3.dnd5th3backend.controller.dto.comment;

import com.dnd5th3.dnd5th3backend.domain.comment.Comment;
import com.dnd5th3.dnd5th3backend.domain.member.Member;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentResponseDto {

    private  String memberEmail;
    private  Long postsId;
    private  Long commentId;
    private  Long groupNo;
    private  Integer commentLayer;
    private  Integer commentOrder;
    private  String content;

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
