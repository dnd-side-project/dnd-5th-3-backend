package com.dnd5th3.dnd5th3backend.controller;

import com.dnd5th3.dnd5th3backend.controller.dto.post.*;
import com.dnd5th3.dnd5th3backend.domain.member.Member;
import com.dnd5th3.dnd5th3backend.domain.posts.Posts;
import com.dnd5th3.dnd5th3backend.domain.vote.Vote;
import com.dnd5th3.dnd5th3backend.domain.vote.VoteType;
import com.dnd5th3.dnd5th3backend.domain.vote.vo.VoteRatioVo;
import com.dnd5th3.dnd5th3backend.service.PostsService;
import com.dnd5th3.dnd5th3backend.service.VoteService;
import com.dnd5th3.dnd5th3backend.utils.S3Uploader;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        Vote voteResult = voteService.getVoteResult(member, foundPost);
        VoteType currentMemberVoteResult = voteResult == null ? VoteType.NO_RESULT : voteResult.getResult();
        String productImageUrl = foundPost.getProductImageUrl() == null ? "" : foundPost.getProductImageUrl();

        return PostResponseDto.builder()
                .id(foundPost.getId())
                .name(foundPost.getMember().getName())
                .title(foundPost.getTitle())
                .content(foundPost.getContent())
                .productImageUrl(productImageUrl)
                .isVoted(foundPost.getIsVoted())
                .permitCount(foundPost.getPermitCount())
                .rejectCount(foundPost.getRejectCount())
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

    @DeleteMapping("/api/v1/posts/{id}")
    public IdResponseDto deletePost(@PathVariable(name = "id") Long id, @AuthenticationPrincipal Member member) {
        postsService.deletePost(id, member);
        return IdResponseDto.builder().id(id).build();
    }

    @GetMapping("/api/v1/posts")
    public ResponseEntity<AllPostResponseDto> findAllPosts(@RequestParam(name = "sorted") String sortType) {
        AllPostResponseDto responseDto = postsService.findAllPostsWithSortType(sortType);
        return ResponseEntity.ok(responseDto);
    }

    @PostMapping("/api/v1/posts/{id}/vote")
    public IdResponseDto votePost(@PathVariable(name = "id") Long id, @AuthenticationPrincipal Member member, @RequestBody VoteRequestDto requestDto) {
        Posts posts = postsService.findPostById(id);
        voteService.saveVote(member, posts, requestDto.getResult());

        return IdResponseDto.builder().id(posts.getId()).build();
    }

    @GetMapping("/api/v1/posts/main")
    public AllResponseDto mainPosts() {
        Map<String, Posts> mainPostsMap = postsService.findMainPosts();
        Map<String, MainPostDto> resultMap = new HashMap<>();
        List<MainPostDto> resultList = new ArrayList<>();
        mainPostsMap.forEach((key, value) -> {
            VoteRatioVo ratioVo = new VoteRatioVo(value);
            String productImageUrl = value.getProductImageUrl() == null ? "" : value.getProductImageUrl();
            MainPostDto postDto = MainPostDto.builder()
                    .id(value.getId())
                    .name(value.getMember().getName())
                    .title(value.getTitle())
                    .productImageUrl(productImageUrl)
                    .isVoted(value.getIsVoted())
                    .permitRatio(ratioVo.getPermitRatio())
                    .rejectRatio(ratioVo.getRejectRatio())
                    .createdDate(value.getCreatedDate())
                    .voteDeadline(value.getVoteDeadline())
                    .build();
            resultMap.put(key, postDto);
        });

        if (resultMap.get("neckAndNeckPost") == null) {
            MainPostDto mock = MainPostDto.builder()
                    .id(-1L)
                    .name("no content")
                    .title("no content")
                    .productImageUrl("no content")
                    .isVoted(true)
                    .permitRatio(-99L)
                    .rejectRatio(-99L)
                    .createdDate(LocalDateTime.now())
                    .voteDeadline(LocalDateTime.now())
                    .build();
            resultMap.put("neckAndNeckPost", mock);
        }

        if (resultMap.get("bestResponsePost") == null) {
            MainPostDto mock = MainPostDto.builder()
                    .id(-1L)
                    .name("no content")
                    .title("no content")
                    .productImageUrl("no content")
                    .isVoted(true)
                    .permitRatio(-99L)
                    .rejectRatio(-99L)
                    .createdDate(LocalDateTime.now())
                    .voteDeadline(LocalDateTime.now())
                    .build();
            resultMap.put("bestResponsePost", mock);
        }

        for (MainPostDto value : resultMap.values()) {
            resultList.add(value);
        }

        return new AllResponseDto(resultList);
    }

}
