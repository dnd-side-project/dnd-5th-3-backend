package com.dnd5th3.dnd5th3backend.domain.comment;

import com.dnd5th3.dnd5th3backend.controller.dto.comment.CommentRequestDto;
import com.dnd5th3.dnd5th3backend.domain.common.BaseTime;
import com.dnd5th3.dnd5th3backend.domain.member.Member;
import com.dnd5th3.dnd5th3backend.domain.posts.Posts;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.transaction.Transactional;

@Builder
@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Comment extends BaseTime {

    @Id
    @Column(name = "comment_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long groupNo;

    private Integer commentLayer;

    private Integer commentOrder;

    private String content;

    @Column(columnDefinition = "boolean default false")
    private Boolean isDeleted;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    private Posts posts;

    public static Comment create(CommentRequestDto requestDto,Member member) {
        return Comment.builder()
                .member(member)
                .id(requestDto.getCommentId())
                .groupNo(requestDto.getGroupNo())
                .commentLayer(requestDto.getCommentLayer())
                .commentOrder(requestDto.getCommentOrder())
                .content(requestDto.getContent())
                .build();
    }

    public void update(CommentRequestDto requestDto){
        this.content = requestDto.getContent();
    }
}
