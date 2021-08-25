package com.dnd5th3.dnd5th3backend.controller;

import com.dnd5th3.dnd5th3backend.controller.dto.mypage.InfoResponseDto;
import com.dnd5th3.dnd5th3backend.controller.dto.post.PostsListDto;
import com.dnd5th3.dnd5th3backend.domain.member.Member;
import com.dnd5th3.dnd5th3backend.domain.posts.Posts;
import com.dnd5th3.dnd5th3backend.domain.vo.VoteRatioVo;
import com.dnd5th3.dnd5th3backend.service.PostsService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
public class MyPageController {

    private final PostsService postsService;

    @GetMapping("/api/v1/mypage")
    public InfoResponseDto findMemberInfo(@AuthenticationPrincipal Member member, @RequestParam String sorted) {
        List<Posts> postsList = postsService.findAllPostsByMember(member, sorted);
        List<PostsListDto> dtoList = postsList.stream().map(p -> {
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

        return InfoResponseDto.builder()
                .name(member.getName())
                .email(member.getEmail())
                .posts(dtoList)
                .build();
    }

}
