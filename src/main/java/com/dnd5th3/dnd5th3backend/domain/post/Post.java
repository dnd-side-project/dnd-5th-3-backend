package com.dnd5th3.dnd5th3backend.domain.post;

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
@Entity
public class Post extends BaseTime {

    @Id
    @Column(name = "post_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id")
    private Member member;

    @NotNull
    @Column
    private String title;

    @NotNull
    @Column
    private String productName;

    @NotNull
    @Column
    private String content;

    @Column
    private String productImageUrl;

    @NotNull
    @Column
    private boolean isVoted = false;

    @NotNull
    @Column
    private int permitCount = 0;

    @NotNull
    @Column
    private int rejectCount = 0;

    @NotNull
    @Column
    private int viewCount = 0;

    @NotNull
    @Column
    private boolean isDeleted = false;

    @Builder
    public Post(Long id, Member member, String title, String productName, String content, String productImageUrl) {
        this.id = id;
        this.member = member;
        this.title = title;
        this.productName = productName;
        this.content = content;
        this.productImageUrl = productImageUrl;
    }

}
