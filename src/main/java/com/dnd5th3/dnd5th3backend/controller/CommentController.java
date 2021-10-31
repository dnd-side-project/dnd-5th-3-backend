package com.dnd5th3.dnd5th3backend.controller;

import com.dnd5th3.dnd5th3backend.controller.dto.comment.CommentListResponseDto;
import com.dnd5th3.dnd5th3backend.controller.dto.comment.CommentRequestDto;
import com.dnd5th3.dnd5th3backend.controller.dto.comment.CommentResponseDto;
import com.dnd5th3.dnd5th3backend.domain.comment.Comment;
import com.dnd5th3.dnd5th3backend.domain.member.Member;
import com.dnd5th3.dnd5th3backend.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/comment")
public class CommentController {

    private final CommentService commentService;
    private final ModelMapper modelMapper;

    @PostMapping
    public ResponseEntity<CommentResponseDto> saveAPI(@RequestBody CommentRequestDto requestDto, @AuthenticationPrincipal Member member){
        Comment comment = commentService.saveComment(requestDto, member);
        CommentResponseDto commentResponseDto = modelMapper.map(comment, CommentResponseDto.class);
        return ResponseEntity.status(HttpStatus.CREATED).body(commentResponseDto);
    }

    @PutMapping
    public ResponseEntity<CommentResponseDto> editAPI(@RequestBody CommentRequestDto requestDto){
        Comment comment = commentService.editComment(requestDto);
        CommentResponseDto commentResponseDto = modelMapper.map(comment, CommentResponseDto.class);

        return ResponseEntity.ok(commentResponseDto);
    }

    @DeleteMapping
    public ResponseEntity<CommentResponseDto> deleteAPI(@RequestBody CommentRequestDto requestDto){
        Comment comment = commentService.deleteComment(requestDto);
        CommentResponseDto commentResponseDto = modelMapper.map(comment, CommentResponseDto.class);

        return ResponseEntity.ok(commentResponseDto);
    }

    @GetMapping("/{postId}/posts")
    public ResponseEntity<CommentListResponseDto> getListAPI(@PathVariable Long postId, @RequestParam("pageNum") Integer pageNum, @AuthenticationPrincipal Member member){
        CommentListResponseDto commentListResponseDto = commentService.getCommentList(postId,pageNum,member);
        return ResponseEntity.ok(commentListResponseDto);
    }

    @GetMapping("/{commentId}")
    public ResponseEntity<CommentListResponseDto> getDetailAPI(@PathVariable Long commentId, @AuthenticationPrincipal Member member){
        CommentListResponseDto detailComment = commentService.getDetailComment(commentId,member);
        return ResponseEntity.ok(detailComment);
    }

    @PostMapping("/{commentId}/reply")
    public ResponseEntity<CommentResponseDto> saveReplyAPI(@PathVariable Long commentId, @RequestBody CommentRequestDto requestDto, @AuthenticationPrincipal Member member){
        Comment comment = commentService.saveReplyComment(requestDto,commentId ,member);
        CommentResponseDto commentResponseDto = modelMapper.map(comment, CommentResponseDto.class);
        return ResponseEntity.status(HttpStatus.CREATED).body(commentResponseDto);
    }

}
