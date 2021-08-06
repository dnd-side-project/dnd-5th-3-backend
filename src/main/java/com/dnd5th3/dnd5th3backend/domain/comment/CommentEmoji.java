package com.dnd5th3.dnd5th3backend.domain.comment;


import com.dnd5th3.dnd5th3backend.domain.emoji.Emoji;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;

@Builder
@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
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

    private Integer emojiCount;

}

