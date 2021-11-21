package com.dnd5th3.dnd5th3backend.controller;

import com.dnd5th3.dnd5th3backend.controller.dto.mypage.InfoResponseDto;
import com.dnd5th3.dnd5th3backend.controller.dto.post.PostsListDto;
import com.dnd5th3.dnd5th3backend.domain.member.Member;
import com.dnd5th3.dnd5th3backend.domain.posts.Posts;
import com.dnd5th3.dnd5th3backend.domain.vote.vo.VoteRatioVo;
import com.dnd5th3.dnd5th3backend.service.MyPageService;
import com.dnd5th3.dnd5th3backend.service.PostsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
public class MyPageController {

    private final MyPageService myPageService;

    @GetMapping("/api/v1/mypage")
    public ResponseEntity<InfoResponseDto> findMemberInfo(@AuthenticationPrincipal Member member, @RequestParam String sorted) {
        InfoResponseDto infoResponseDto = myPageService.findMemberInfoWithSortType(member, sorted);
        return ResponseEntity.ok(infoResponseDto);
    }

}
