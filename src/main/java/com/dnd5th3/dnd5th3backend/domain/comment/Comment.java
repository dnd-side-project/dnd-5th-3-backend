package com.dnd5th3.dnd5th3backend.domain.comment;

import com.dnd5th3.dnd5th3backend.controller.dto.comment.CommentRequestDto;
import com.dnd5th3.dnd5th3backend.domain.common.BaseTime;
import com.dnd5th3.dnd5th3backend.domain.member.Member;
import com.dnd5th3.dnd5th3backend.domain.posts.Posts;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.List;

@Builder
@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@DynamicUpdate
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

    public static Comment create(CommentRequestDto requestDto,long groupNo,int commentLayer,int commentOrder,Member member,Posts posts) {
        Comment comment = Comment.builder()
                .groupNo(groupNo)
                .commentLayer(commentLayer)
                .commentOrder(commentOrder)
                .content(requestDto.getContent())
                .isDeleted(Boolean.FALSE)
                .member(member)
                .posts(posts)
                .build();
        posts.addComment(comment);

        return comment;
    }

    public void update(CommentRequestDto requestDto){
        this.content = requestDto.getContent();
    }
    public void delete(){
        this.isDeleted = true;
    }
}
