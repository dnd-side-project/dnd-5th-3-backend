package com.dnd5th3.dnd5th3backend.controller;

import com.dnd5th3.dnd5th3backend.controller.dto.post.*;
import com.dnd5th3.dnd5th3backend.domain.member.Member;
import com.dnd5th3.dnd5th3backend.domain.posts.Posts;
import com.dnd5th3.dnd5th3backend.service.PostsService;
import com.dnd5th3.dnd5th3backend.service.VoteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
public class PostsController {

    private final PostsService postsService;
    private final VoteService voteService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/api/v1/posts")
    public IdResponseDto savePost(@RequestBody SaveRequestDto postSaveRequestDto, @AuthenticationPrincipal Member member) {
        Posts savedPosts = postsService.savePost(member, postSaveRequestDto.getTitle(), postSaveRequestDto.getProductName(), postSaveRequestDto.getContent(), postSaveRequestDto.getProductImageUrl());

        return IdResponseDto.builder().id(savedPosts.getId()).build();
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
                .voteDeadline(foundPost.getVoteDeadline())
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
                .voteDeadline(updatedPost.getVoteDeadline())
                .build();
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/api/v1/posts/{id}")
    public DeleteResponseDto deletePost(@PathVariable(name = "id") Long id) {
        postsService.deletePost(id);
        return DeleteResponseDto.builder().id(id).build();
    }

    @GetMapping("/api/v1/posts")
    public AllResponseDto findPostsList(@RequestParam String sorted, @RequestParam int offset) {
        List<Posts> postsList = postsService.findAllPosts(sorted, offset);
        List<PostsListDto> dtoList = postsList.stream().map( p -> {
            CalculateRatioDto ratioDto = CalculateRatioDto.calculate(p);
            return PostsListDto.builder()
                    .name(p.getMember().getName())
                    .title(p.getTitle())
                    .productImageUrl(p.getProductImageUrl())
                    .isVoted(p.getIsVoted())
                    .permitRatio(ratioDto.getPermitRatio())
                    .rejectRatio(ratioDto.getRejectRatio())
                    .createdDate(p.getCreatedDate())
                    .build();
        }).collect(Collectors.toList());

        return new AllResponseDto(dtoList);
    }

    @PostMapping("/api/v1/posts/{id}/vote")
    public IdResponseDto votePost(@PathVariable(name = "id") Long id, @AuthenticationPrincipal Member member) {
        Posts posts = postsService.findPostById(id);
        voteService.saveVote(member, posts);

        return IdResponseDto.builder().id(posts.getId()).build();
    }
}
