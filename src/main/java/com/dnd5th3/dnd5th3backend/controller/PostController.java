package com.dnd5th3.dnd5th3backend.controller;

import com.dnd5th3.dnd5th3backend.controller.dto.post.SaveRequestDto;
import com.dnd5th3.dnd5th3backend.controller.dto.post.SaveResponseDto;
import com.dnd5th3.dnd5th3backend.domain.member.Member;
import com.dnd5th3.dnd5th3backend.domain.posts.Posts;
import com.dnd5th3.dnd5th3backend.service.MemberServiceImpl;
import com.dnd5th3.dnd5th3backend.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class PostController {

    private final PostService postService;
    private final MemberServiceImpl memberService;

    @PostMapping("/api/v1/posts")
    public SaveResponseDto savePost(@RequestBody SaveRequestDto postSaveRequestDto) {
        Member writer = memberService.findMemberById(postSaveRequestDto.getMemberId());
        Posts savedPosts = postService.savePost(writer, postSaveRequestDto.getTitle(), postSaveRequestDto.getProductName(), postSaveRequestDto.getContent(), postSaveRequestDto.getProductImageUrl());

        return SaveResponseDto.builder().id(savedPosts.getId()).build();
    }
}
