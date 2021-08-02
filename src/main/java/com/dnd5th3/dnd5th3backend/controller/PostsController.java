package com.dnd5th3.dnd5th3backend.controller;

import com.dnd5th3.dnd5th3backend.controller.dto.post.PostResponseDto;
import com.dnd5th3.dnd5th3backend.controller.dto.post.SaveRequestDto;
import com.dnd5th3.dnd5th3backend.controller.dto.post.SaveResponseDto;
import com.dnd5th3.dnd5th3backend.domain.member.Member;
import com.dnd5th3.dnd5th3backend.domain.posts.Posts;
import com.dnd5th3.dnd5th3backend.service.MemberService;
import com.dnd5th3.dnd5th3backend.service.PostsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class PostsController {

    private final PostsService postsService;
    private final MemberService memberService;

    @PostMapping("/api/v1/posts")
    public SaveResponseDto savePost(@RequestBody SaveRequestDto postSaveRequestDto) {
        Member writer = memberService.findMemberById(postSaveRequestDto.getMemberId());
        Posts savedPosts = postsService.savePost(writer, postSaveRequestDto.getTitle(), postSaveRequestDto.getProductName(), postSaveRequestDto.getContent(), postSaveRequestDto.getProductImageUrl());

        return SaveResponseDto.builder().id(savedPosts.getId()).build();
    }

    @GetMapping("/api/v1/posts/{id}")
    public PostResponseDto findPostById(@PathVariable(name = "id") Long id) {
        Posts foundPost = postsService.findPostById(id);
        Integer permitCount = foundPost.getPermitCount();
        Integer rejectCount = foundPost.getRejectCount();
        Long permitRatio = Math.round(((double) permitCount / (permitCount + rejectCount)) * 100);
        Long rejectRatio = Math.round(((double) rejectCount / (permitCount + rejectCount)) * 100);

        return PostResponseDto.builder()
                .name(foundPost.getMember().getName())
                .title(foundPost.getTitle())
                .productName(foundPost.getProductName())
                .content(foundPost.getContent())
                .productImageUrl(foundPost.getProductImageUrl())
                .isVoted(foundPost.getIsVoted())
                .permitRatio(permitRatio)
                .rejectRatio(rejectRatio)
                .createdDate(foundPost.getCreatedDate())
                .build();
    }
}
