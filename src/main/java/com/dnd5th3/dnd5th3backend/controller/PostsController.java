package com.dnd5th3.dnd5th3backend.controller;

import com.dnd5th3.dnd5th3backend.controller.dto.post.*;
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
        CalculateRatioDto ratioDto = CalculateRatioDto.calculate(foundPost);

        return PostResponseDto.builder()
                .name(foundPost.getMember().getName())
                .title(foundPost.getTitle())
                .productName(foundPost.getProductName())
                .content(foundPost.getContent())
                .productImageUrl(foundPost.getProductImageUrl())
                .isVoted(foundPost.getIsVoted())
                .permitRatio(ratioDto.getPermitRatio())
                .rejectRatio(ratioDto.getRejectRatio())
                .createdDate(foundPost.getCreatedDate())
                .build();
    }

    @PostMapping("/api/v1/posts/{id}")
    public PostResponseDto updatePost(@PathVariable(name = "id") Long id, @RequestBody UpdateRequestDto updateRequestDto) {
        Posts updatedPost = postsService.updatePost(id, updateRequestDto.getTitle(), updateRequestDto.getProductName(), updateRequestDto.getContent(), updateRequestDto.getProductImageUrl());
        CalculateRatioDto ratioDto = CalculateRatioDto.calculate(updatedPost);

        return PostResponseDto.builder()
                .name(updatedPost.getMember().getName())
                .title(updatedPost.getTitle())
                .productName(updatedPost.getProductName())
                .content(updatedPost.getContent())
                .productImageUrl(updatedPost.getProductImageUrl())
                .isVoted(updatedPost.getIsVoted())
                .permitRatio(ratioDto.getPermitRatio())
                .rejectRatio(ratioDto.getRejectRatio())
                .createdDate(updatedPost.getCreatedDate())
                .build();
    }

    @DeleteMapping("/api/v1/posts/{id}")
    public Long deletePost(@PathVariable(name = "id") Long id) {
        postsService.deletePost(id);
        return id;
    }
}
