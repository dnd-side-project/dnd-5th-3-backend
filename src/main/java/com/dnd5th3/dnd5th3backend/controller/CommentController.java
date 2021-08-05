package com.dnd5th3.dnd5th3backend.controller;

import com.dnd5th3.dnd5th3backend.controller.dto.comment.CommentRequestDto;
import com.dnd5th3.dnd5th3backend.controller.dto.comment.CommentResponseDto;
import com.dnd5th3.dnd5th3backend.domain.comment.Comment;
import com.dnd5th3.dnd5th3backend.domain.member.Member;
import com.dnd5th3.dnd5th3backend.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/comment")
public class CommentController {

    private final CommentService commentService;

    @PostMapping
    public ResponseEntity<CommentResponseDto> saveAPI(@RequestBody CommentRequestDto requestDto, @AuthenticationPrincipal Member member){
        Comment comment = commentService.saveComment(requestDto, member);
        CommentResponseDto commentResponseDto  = CommentResponseDto.of(comment, member);
        return ResponseEntity.ok(commentResponseDto);
    }

    @PutMapping
    public ResponseEntity<CommentResponseDto> editAPI(@RequestBody CommentRequestDto requestDto){
        long commentId = commentService.editComment(requestDto);

        CommentResponseDto commentResponseDto = CommentResponseDto.builder()
                .commentId(commentId)
                .build();

        return ResponseEntity.ok(commentResponseDto);
    }

}
