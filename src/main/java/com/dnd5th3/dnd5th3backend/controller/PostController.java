package com.dnd5th3.dnd5th3backend.controller;

import com.dnd5th3.dnd5th3backend.controller.dto.post.PostSaveRequestDto;
import com.dnd5th3.dnd5th3backend.controller.dto.post.PostSaveResponseDto;
import com.dnd5th3.dnd5th3backend.domain.member.Member;
import com.dnd5th3.dnd5th3backend.domain.post.Post;
import com.dnd5th3.dnd5th3backend.service.MemberServiceImpl;
import com.dnd5th3.dnd5th3backend.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/v1/posts")
@RestController
public class PostController {

    private final PostService postService;
    private final MemberServiceImpl memberService;

    @PostMapping("/")
    public PostSaveResponseDto savePost(@RequestBody PostSaveRequestDto postSaveRequestDto) {
        Member writer = memberService.findMemberById(postSaveRequestDto.getMemberId());
        Post savedPost = postService.savePost(writer, postSaveRequestDto.getTitle(), postSaveRequestDto.getProductName(), postSaveRequestDto.getContent(), postSaveRequestDto.getProductImageUrl());

        return PostSaveResponseDto.builder()
                .name(savedPost.getMember().getName())
                .title(savedPost.getTitle())
                .productName(savedPost.getProductName())
                .content(savedPost.getContent())
                .productImageUrl(savedPost.getProductImageUrl())
                .permitRatio(0)
                .rejectRatio(0)
                .createdDate(savedPost.getCreatedDate())
                .build();
    }
}
