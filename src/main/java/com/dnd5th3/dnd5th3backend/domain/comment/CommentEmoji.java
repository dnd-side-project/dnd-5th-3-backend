package com.dnd5th3.dnd5th3backend.domain.comment;


import com.dnd5th3.dnd5th3backend.domain.emoji.Emoji;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

import java.util.List;

import static javax.persistence.FetchType.LAZY;

@Builder
@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@DynamicInsert
@DynamicUpdate
public class CommentEmoji {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_emoji_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "comment_id")
    private Comment comment;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "emoji_id")
    private Emoji emoji;

    private Integer commentEmojiCount;

    @OneToMany(fetch = LAZY,mappedBy = "commentEmoji",cascade = CascadeType.ALL)
    private List<CommentEmojiMember> commentEmojiMembers;

    public boolean update(boolean isChecked) {
        if(isChecked){
            this.commentEmojiCount += 1;
        }else {
            this.commentEmojiCount -= 1;
        }
        // 이모지 클릭이 0이 될 경우 제거 필요
        return this.commentEmojiCount == 0;
    }
}

