package com.dnd5th3.dnd5th3backend.domain.posts;

import com.dnd5th3.dnd5th3backend.domain.common.BaseTime;
import com.dnd5th3.dnd5th3backend.domain.member.Member;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Entity
public class Posts extends BaseTime {

    @Id
    @Column(name = "post_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id")
    private Member member;

    @NotNull
    private String title;

    @NotNull
    private String productName;

    @NotNull
    private String content;

    private String productImageUrl;

    @NotNull
    private Boolean isVoted;

    @NotNull
    private Integer permitCount;

    @NotNull
    private Integer rejectCount;

    @NotNull
    private Integer viewCount;

    @NotNull
    private Boolean isDeleted;

}
