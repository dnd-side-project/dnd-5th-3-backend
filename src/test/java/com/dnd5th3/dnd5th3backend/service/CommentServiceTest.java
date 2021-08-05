package com.dnd5th3.dnd5th3backend.service;

import com.dnd5th3.dnd5th3backend.controller.dto.comment.CommentRequestDto;
import com.dnd5th3.dnd5th3backend.domain.comment.Comment;
import com.dnd5th3.dnd5th3backend.domain.member.Member;
import com.dnd5th3.dnd5th3backend.domain.member.Role;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.Rollback;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Import(CommentService.class)
@TestMethodOrder(value = MethodOrderer.OrderAnnotation.class)
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

    @Order(1)
    @DisplayName("댓글 등록 테스트")
    @Test
    @Rollback(value = false)
    void saveComment() {
        CommentRequestDto commentRequestDto = new CommentRequestDto(1L, 1L, 1L, 0, 0, "comment test");
        Comment comment = commentService.saveComment(commentRequestDto, member);
        
        assertEquals(commentRequestDto.getContent(),comment.getContent(),"요청한 댓글 내용과 DB 저장된 댓글이 일치하는지 확인");
    }
    
    @Order(2)
    @DisplayName("댓글 수정 테스트")
    @Test
    void updateComment() {

        CommentRequestDto commentRequestDto = new CommentRequestDto(null, 1L, null, null, null, "comment update");
        long commentId = commentService.editComment(commentRequestDto);
        assertEquals(commentRequestDto.getCommentId(),commentId,"응답에 오류가 없는지 확인");
    }

    @Order(3)
    @DisplayName("댓글 삭제 테스트")
    @Test
    void deleteComment() {

        CommentRequestDto commentRequestDto = new CommentRequestDto(null, 1L, null, null, null, null);
        long commentId = commentService.deleteComment(commentRequestDto);
        assertEquals(commentRequestDto.getCommentId(),commentId,"응답에 오류가 없는지 확인");
    }
}