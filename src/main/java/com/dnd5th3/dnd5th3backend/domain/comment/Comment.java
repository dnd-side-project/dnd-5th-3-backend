package com.dnd5th3.dnd5th3backend.domain.comment;

import com.dnd5th3.dnd5th3backend.controller.dto.comment.CommentRequestDto;
import com.dnd5th3.dnd5th3backend.domain.common.BaseTime;
import com.dnd5th3.dnd5th3backend.domain.member.Member;
import com.dnd5th3.dnd5th3backend.domain.posts.Posts;
import lombok.*;

import javax.persistence.*;
import java.util.List;

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

    private Boolean isDeleted;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Posts posts;

    @OneToMany(fetch = FetchType.LAZY,mappedBy = "comment")
    private List<CommentEmoji> commentEmoji;

    public static Comment create(CommentRequestDto requestDto,Member member,Posts posts) {
        return Comment.builder()
                .member(member)
                .posts(posts)
                .id(requestDto.getCommentId())
                .groupNo(requestDto.getGroupNo())
                .commentLayer(requestDto.getCommentLayer())
                .commentOrder(requestDto.getCommentOrder())
                .content(requestDto.getContent())
                .isDeleted(Boolean.FALSE)
                .build();
    }

    public void update(CommentRequestDto requestDto){
        this.content = requestDto.getContent();
    }
    public void delete(){
        this.isDeleted = true;
    }
}
