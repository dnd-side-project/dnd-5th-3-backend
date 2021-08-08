package com.dnd5th3.dnd5th3backend.service;

import com.dnd5th3.dnd5th3backend.config.QuerydslConfig;
import com.dnd5th3.dnd5th3backend.controller.dto.comment.CommentListResponseDto;
import com.dnd5th3.dnd5th3backend.controller.dto.comment.CommentRequestDto;
import com.dnd5th3.dnd5th3backend.controller.dto.comment.CommentResponseDto;
import com.dnd5th3.dnd5th3backend.domain.comment.Comment;
import com.dnd5th3.dnd5th3backend.domain.member.Member;
import com.dnd5th3.dnd5th3backend.repository.MemberRepository;
import org.junit.jupiter.api.*;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;


import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Import({CommentService.class, QuerydslConfig.class})
@ActiveProfiles("h2")
class CommentServiceTest {

    @Autowired
    private CommentService commentService;

    @Autowired
    private MemberRepository memberRepository;

    private Member member;

    @Autowired
    private ModelMapper modelMapper;

    @BeforeEach
    public void setup(){
        long memberId = 1;
        member = memberRepository.getById(memberId);
    }

    @DisplayName("댓글 등록 테스트")
    @Test
    void saveComment() {
        CommentRequestDto commentRequestDto = new CommentRequestDto(1L, null, 1L, 0, 0, "comment test");
        Comment comment = commentService.saveComment(commentRequestDto, member);

        CommentResponseDto commentResponseDto = modelMapper.map(comment, CommentResponseDto.class);

        assertEquals(commentRequestDto.getContent(),commentResponseDto.getContent(),"요청한 댓글 내용과 DB 저장된 댓글이 일치하는지 확인");
        assertNotNull(commentResponseDto.getCommentId(),"댓글 ID 반환 확인");
    }

    @DisplayName("댓글 삭제 테스트")
    @Test
    void getComment() {

        CommentRequestDto commentRequestDto = new CommentRequestDto(null, 1L, null, null, null, null);
        Comment comment = commentService.deleteComment(commentRequestDto);
        assertEquals(commentRequestDto.getCommentId(),comment.getId(),"응답에 오류가 없는지 확인(삭제)");
    }
    
    @DisplayName("댓글 수정 테스트")
    @Test
    void updateComment() {

        CommentRequestDto commentRequestDto = new CommentRequestDto(null, 1L, null, null, null, "comment update");
        Comment comment = commentService.editComment(commentRequestDto);
        assertEquals(commentRequestDto.getCommentId(),comment.getId(),"응답에 오류가 없는지 확인(수정)");
    }

    @DisplayName("댓글 목록 조회 테스트")
    @Test
    void getCommentList() {
        long expectedTotalCount = 6;
        CommentListResponseDto commentListResponseDto = commentService.getCommentList(1,0,member);
        assertEquals(expectedTotalCount,commentListResponseDto.getTotalCount(),"총 댓글 개수 확인");
    }
    
}