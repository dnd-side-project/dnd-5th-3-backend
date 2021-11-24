package com.dnd5th3.dnd5th3backend.controller.dto.post;

import com.dnd5th3.dnd5th3backend.domain.posts.Posts;
import com.dnd5th3.dnd5th3backend.domain.vote.vo.VoteRatioVo;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Builder
@Getter
public class PostsListDto {

    private Long id;
    private String name;
    private String title;
    private String productImageUrl;
    private Boolean isVoted;
    private Long permitRatio;
    private Long rejectRatio;
    private LocalDateTime createdDate;
    private LocalDateTime voteDeadline;

    public static List<PostsListDto> makePostsToListDtos(List<Posts> posts) {
        return posts.stream().map(p -> {
            VoteRatioVo ratioVo = new VoteRatioVo(p);
            String productImageUrl = p.getProductImageUrl() == null ? "" : p.getProductImageUrl();
            return PostsListDto.builder()
                    .id(p.getId())
                    .name(p.getMember().getName())
                    .title(p.getTitle())
                    .productImageUrl(productImageUrl)
                    .isVoted(p.getIsVoted())
                    .permitRatio(ratioVo.getPermitRatio())
                    .rejectRatio(ratioVo.getRejectRatio())
                    .createdDate(p.getCreatedDate())
                    .voteDeadline(p.getVoteDeadline())
                    .build();
        }).collect(Collectors.toList());
    }
}
