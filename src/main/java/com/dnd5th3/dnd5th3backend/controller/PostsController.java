package com.dnd5th3.dnd5th3backend.controller;

import com.dnd5th3.dnd5th3backend.controller.dto.post.*;
import com.dnd5th3.dnd5th3backend.domain.member.Member;
import com.dnd5th3.dnd5th3backend.domain.posts.Posts;
import com.dnd5th3.dnd5th3backend.domain.vo.VoteRatioVo;
import com.dnd5th3.dnd5th3backend.domain.vote.Vote;
import com.dnd5th3.dnd5th3backend.domain.vote.VoteType;
import com.dnd5th3.dnd5th3backend.service.PostsService;
import com.dnd5th3.dnd5th3backend.service.VoteService;
import com.dnd5th3.dnd5th3backend.utils.S3Uploader;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
public class PostsController {

    private final PostsService postsService;
    private final VoteService voteService;
    private final S3Uploader s3Uploader;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/api/v1/posts")
    public IdResponseDto savePost(@RequestPart String title,
                                  @RequestPart String content,
                                  @RequestPart MultipartFile file,
                                  @AuthenticationPrincipal Member member) throws IOException{
        String productImageUrl = s3Uploader.upload(file, "static");
        Posts savedPosts = postsService.savePost(member, title, content, productImageUrl);

        return IdResponseDto.builder().id(savedPosts.getId()).build();
    }

    @GetMapping("/api/v1/posts/{id}")
    public PostResponseDto findPostById(@PathVariable(name = "id") Long id, @AuthenticationPrincipal Member member) {
        Posts foundPost = postsService.findPostById(id);
        VoteRatioVo ratioVo = new VoteRatioVo(foundPost);
        Vote voteResult = voteService.getVoteResult(member, foundPost);
        VoteType currentMemberVoteResult = voteResult == null ? VoteType.NO_RESULT : voteResult.getResult();

        return PostResponseDto.builder()
                .name(foundPost.getMember().getName())
                .title(foundPost.getTitle())
                .content(foundPost.getContent())
                .productImageUrl(foundPost.getProductImageUrl())
                .isVoted(foundPost.getIsVoted())
                .permitRatio(ratioVo.getPermitRatio())
                .rejectRatio(ratioVo.getRejectRatio())
                .createdDate(foundPost.getCreatedDate())
                .voteDeadline(foundPost.getVoteDeadline())
                .currentMemberVoteResult(currentMemberVoteResult)
                .build();
    }

    @PostMapping("/api/v1/posts/{id}")
    public IdResponseDto updatePost(@PathVariable(name = "id") Long id,
                                    @RequestPart String title,
                                    @RequestPart String content,
                                    @RequestPart MultipartFile file
                                    ) throws IOException {
        String productImageUrl = s3Uploader.upload(file, "static");
        Posts updatedPost = postsService.updatePost(id, title, content, productImageUrl);
        return IdResponseDto.builder().id(updatedPost.getId()).build();
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/api/v1/posts/{id}")
    public IdResponseDto deletePost(@PathVariable(name = "id") Long id) {
        postsService.deletePost(id);
        return IdResponseDto.builder().id(id).build();
    }

    @GetMapping("/api/v1/posts")
    public AllResponseDto findPostsList(@RequestParam String sorted, @RequestParam int offset) {
        List<Posts> postsList = postsService.findAllPosts(sorted, offset);
        List<PostsListDto> dtoList = postsList.stream().map(p -> {
            VoteRatioVo ratioVo = new VoteRatioVo(p);
            return PostsListDto.builder()
                    .name(p.getMember().getName())
                    .title(p.getTitle())
                    .productImageUrl(p.getProductImageUrl())
                    .isVoted(p.getIsVoted())
                    .permitRatio(ratioVo.getPermitRatio())
                    .rejectRatio(ratioVo.getRejectRatio())
                    .createdDate(p.getCreatedDate())
                    .build();
        }).collect(Collectors.toList());

        return new AllResponseDto(dtoList);
    }

    @PostMapping("/api/v1/posts/{id}/vote")
    public IdResponseDto votePost(@PathVariable(name = "id") Long id, @AuthenticationPrincipal Member member, @RequestBody VoteRequestDto requestDto) {
        Posts posts = postsService.findPostById(id);
        voteService.saveVote(member, posts, requestDto.getResult());

        return IdResponseDto.builder().id(posts.getId()).build();
    }

    @GetMapping("/api/v1/posts/main")
    public Map<String, MainPostDto> mainPosts() {
        Map<String, Posts> mainPostsMap = postsService.findMainPosts();
        Map<String, MainPostDto> resultMap = new HashMap<>();
        mainPostsMap.forEach((key, value) -> {
            VoteRatioVo ratioVo = new VoteRatioVo(value);
            MainPostDto postDto = MainPostDto.builder()
                    .name(value.getMember().getName())
                    .title(value.getTitle())
                    .productImageUrl(value.getProductImageUrl())
                    .isVoted(value.getIsVoted())
                    .permitRatio(ratioVo.getPermitRatio())
                    .rejectRatio(ratioVo.getRejectRatio())
                    .createdDate(value.getCreatedDate())
                    .build();
            resultMap.put(key, postDto);
        });

        return resultMap;
    }

}
