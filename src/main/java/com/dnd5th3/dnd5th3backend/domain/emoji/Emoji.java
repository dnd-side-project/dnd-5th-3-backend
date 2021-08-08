package com.dnd5th3.dnd5th3backend.domain.emoji;


import com.dnd5th3.dnd5th3backend.domain.comment.Comment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Builder
@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Emoji {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "emoji_id")
    private Long id;

    private String detail;

    public static Emoji create(String detail){
        return Emoji.builder()
                .detail(detail)
                .build();
    }

}
