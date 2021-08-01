package com.dnd5th3.dnd5th3backend.domain.posts;

import com.dnd5th3.dnd5th3backend.domain.common.BaseTime;
import com.dnd5th3.dnd5th3backend.domain.member.Member;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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
    private boolean isVoted = false;

    @NotNull
    private int permitCount = 0;

    @NotNull
    private int rejectCount = 0;

    @NotNull
    private int viewCount = 0;

    @NotNull
    private boolean isDeleted = false;

}
