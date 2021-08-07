package com.dnd5th3.dnd5th3backend.service;

import com.dnd5th3.dnd5th3backend.controller.dto.comment.CommentRequestDto;
import com.dnd5th3.dnd5th3backend.domain.comment.Comment;
import com.dnd5th3.dnd5th3backend.domain.member.Member;
import com.dnd5th3.dnd5th3backend.repository.comment.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@RequiredArgsConstructor
@Service
public class CommentService {

    private final CommentRepository commentRepository;

    public Comment saveComment(CommentRequestDto requestDto, Member member){
        Comment comment = Comment.create(requestDto, member);
        return commentRepository.save(comment);
    }

    @Transactional
    public long editComment(CommentRequestDto requestDto) {
        Comment comment = commentRepository.findById(requestDto.getCommentId())
                                           .orElseThrow(IllegalArgumentException::new);
        comment.update(requestDto);
        return comment.getId();
    }

    @Transactional
    public long deleteComment(CommentRequestDto requestDto) {
        Comment comment = commentRepository.findById(requestDto.getCommentId())
                .orElseThrow(IllegalArgumentException::new);
        comment.delete();
        return comment.getId();
    }
}
