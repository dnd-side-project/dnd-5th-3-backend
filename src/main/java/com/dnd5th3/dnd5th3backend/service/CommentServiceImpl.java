package com.dnd5th3.dnd5th3backend.service;

import com.dnd5th3.dnd5th3backend.controller.dto.comment.CommentRequestDto;
import com.dnd5th3.dnd5th3backend.domain.comment.Comment;
import com.dnd5th3.dnd5th3backend.domain.member.Member;
import com.dnd5th3.dnd5th3backend.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;

    @Override
    public Comment saveComment(CommentRequestDto requestDto, Member member){
        Comment comment = Comment.create(requestDto, member);
        return commentRepository.save(comment);
    }

    @Transactional
    @Override
    public long updateComment(CommentRequestDto requestDto) {
        Comment comment = commentRepository.findById(requestDto.getCommentId())
                                           .orElseThrow(IllegalArgumentException::new);
        comment.update(requestDto);
        return comment.getId();
    }
}
