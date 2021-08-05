package com.dnd5th3.dnd5th3backend.service;

import com.dnd5th3.dnd5th3backend.controller.dto.comment.CommentRequestDto;
import com.dnd5th3.dnd5th3backend.domain.comment.Comment;
import com.dnd5th3.dnd5th3backend.domain.member.Member;
import com.dnd5th3.dnd5th3backend.domain.member.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Import(CommentService.class)
class CommentServiceTest {

    @Autowired
    private CommentService commentService;

    private Member member;

    @BeforeEach
    public void setup(){
        member = Member.builder()
                .id(1L)
                .email("test@gmail.com")
                .password("1234")
                .role(Role.ROLE_USER)
                .name("닉네임")
                .build();
    }

    @DisplayName("댓글 등록 테스트")
    @Test
    void saveComment() {
        CommentRequestDto commentRequestDto = new CommentRequestDto(1L, 1L, 1L, 0, 0, "comment test");
        Comment comment = commentService.saveComment(commentRequestDto, member);
        
        assertEquals(commentRequestDto.getContent(),comment.getContent(),"요청한 댓글 내용과 DB 저장된 댓글이 일치하는지 확인");
    }

    @DisplayName("댓글 수정 테스트")
    @Test
    //@Rollback(value = false)
    void updateComment() {

        CommentRequestDto commentRequestDto = new CommentRequestDto(1L, 1L, 1L, 0, 0, "comment test");
        commentService.saveComment(commentRequestDto, member);

        commentRequestDto = new CommentRequestDto(1L, 1L, 1L, 0, 0, "comment update");
        long commentId = commentService.editComment(commentRequestDto);

        assertEquals(commentRequestDto.getCommentId(),commentId,"댓글 수정이 정상적으로 되었는지 확인");
    }
}