package com.dnd5th3.dnd5th3backend.service;

import com.dnd5th3.dnd5th3backend.controller.dto.comment.CommentRequestDto;
import com.dnd5th3.dnd5th3backend.domain.comment.Comment;
import com.dnd5th3.dnd5th3backend.domain.member.Member;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

public interface CommentService {
    Comment saveComment(CommentRequestDto requestDto, @AuthenticationPrincipal Member member);
}
