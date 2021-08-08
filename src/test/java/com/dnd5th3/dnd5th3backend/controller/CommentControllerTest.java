package com.dnd5th3.dnd5th3backend.controller;

import com.dnd5th3.dnd5th3backend.config.MockSecurityFilter;
import com.dnd5th3.dnd5th3backend.controller.dto.comment.CommentRequestDto;
import com.dnd5th3.dnd5th3backend.domain.member.Member;
import com.dnd5th3.dnd5th3backend.repository.CommentRepository;
import com.dnd5th3.dnd5th3backend.repository.MemberRepository;

import com.dnd5th3.dnd5th3backend.service.CommentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Charsets;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.HashMap;
import java.util.Map;

import static com.dnd5th3.dnd5th3backend.utils.ApiDocumentUtils.getDocumentRequest;
import static com.dnd5th3.dnd5th3backend.utils.ApiDocumentUtils.getDocumentResponse;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
@ActiveProfiles("h2")
class CommentControllerTest {

    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private CommentService commentService;

    @BeforeEach
    void setUp(RestDocumentationContextProvider restDocumentation){
        mvc = MockMvcBuilders.webAppContextSetup(context)
                .apply(documentationConfiguration(restDocumentation))
                .apply(springSecurity(new MockSecurityFilter()))
                .build();
    }

    @DisplayName("댓글 조회 API 테스트")
    @Test
    void getAPI() throws Exception {

        Member member = memberRepository.findByEmail("test@gmail.com");
        ResultActions actions = mvc.perform(RestDocumentationRequestBuilders.get("/api/v1/comment/{postId}",1)
                .principal(new UsernamePasswordAuthenticationToken(member,null))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding(Charsets.UTF_8.toString()));

        actions
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("comment/save",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        requestFields(
                                fieldWithPath("postId").description("글 번호"),
                                fieldWithPath("commentId").description("댓글 번호"),
                                fieldWithPath("groupNo").description("댓글 그룹번호"),
                                fieldWithPath("commentLayer").description("댓글 계층"),
                                fieldWithPath("commentOrder").description("댓글 순서"),
                                fieldWithPath("content").description("댓글 내용")
                        ),
                        responseFields(
                                fieldWithPath("memberEmail").description("사용자 이메일"),
                                fieldWithPath("postId").description("글 번호"),
                                fieldWithPath("commentId").description("댓글 번호"),
                                fieldWithPath("groupNo").description("댓글 그룹번호"),
                                fieldWithPath("commentLayer").description("댓글 계층"),
                                fieldWithPath("commentOrder").description("댓글 순서"),
                                fieldWithPath("content").description("댓글 내용")
                        )
                ))
                .andExpect(jsonPath("$.commentId").value(1L));

    }

    @DisplayName("댓글 등록 API 테스트")
    @Test
    void saveAPI() throws Exception {

        Member member = memberRepository.findByEmail("test@gmail.com");
        CommentRequestDto commentRequestDto = new CommentRequestDto(1L, 1L, 1L, 0, 0, "comment test");

        ResultActions actions = mvc.perform(RestDocumentationRequestBuilders.post("/api/v1/comment")
                .principal(new UsernamePasswordAuthenticationToken(member,null))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding(Charsets.UTF_8.toString())
                .content(objectMapper.writeValueAsString(commentRequestDto)));

        actions
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("comment/save",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        requestFields(
                                fieldWithPath("postId").description("글 번호"),
                                fieldWithPath("commentId").description("댓글 번호"),
                                fieldWithPath("groupNo").description("댓글 그룹번호"),
                                fieldWithPath("commentLayer").description("댓글 계층"),
                                fieldWithPath("commentOrder").description("댓글 순서"),
                                fieldWithPath("content").description("댓글 내용")
                        ),
                        responseFields(
                                fieldWithPath("memberEmail").description("사용자 이메일"),
                                fieldWithPath("postId").description("글 번호"),
                                fieldWithPath("commentId").description("댓글 번호"),
                                fieldWithPath("groupNo").description("댓글 그룹번호"),
                                fieldWithPath("commentLayer").description("댓글 계층"),
                                fieldWithPath("commentOrder").description("댓글 순서"),
                                fieldWithPath("content").description("댓글 내용")
                        )
                ))
                .andExpect(jsonPath("$.commentId").value(1L));

    }

    @DisplayName("댓글 수정 API 테스트")
    @Test
    void editAPI() throws Exception {

        Member member = memberRepository.findByEmail("test@gmail.com");
        CommentRequestDto commentRequestDto = new CommentRequestDto(1L, 1L, 1L, 0, 0, "comment test");
        commentService.saveComment(commentRequestDto,member);
        Map<Object,Object> request = new HashMap<>();
        request.put("commentId",1L);
        request.put("content","comment edit");

        ResultActions actions = mvc.perform(RestDocumentationRequestBuilders.put("/api/v1/comment")
                .principal(new UsernamePasswordAuthenticationToken(member,null))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding(Charsets.UTF_8.toString())
                .content(objectMapper.writeValueAsString(request)));

        actions
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("comment/edit",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        relaxedRequestFields(
                                fieldWithPath("commentId").description("댓글 번호"),
                                fieldWithPath("content").description("수정할 댓글 내용")
                        ),
                        relaxedResponseFields(
                                fieldWithPath("commentId").description("댓글 번호")
                        )
                ))
                .andExpect(jsonPath("$.commentId").value(1L));

    }

    @DisplayName("댓글 삭제 API 테스트")
    @Test
    void deleteAPI() throws Exception {

        Member member = memberRepository.findByEmail("test@gmail.com");
        CommentRequestDto commentRequestDto = new CommentRequestDto(1L, 1L, 1L, 0, 0, "comment test");
        commentService.saveComment(commentRequestDto,member);

        Map<Object,Object> request = new HashMap<>();
        request.put("commentId",1L);

        ResultActions actions = mvc.perform(RestDocumentationRequestBuilders.delete("/api/v1/comment")
                .principal(new UsernamePasswordAuthenticationToken(member,null))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding(Charsets.UTF_8.toString())
                .content(objectMapper.writeValueAsString(request)));

        actions
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("comment/delete",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        relaxedRequestFields(
                                fieldWithPath("commentId").description("삭제할 댓글 번호")
                        ),
                        relaxedResponseFields(
                                fieldWithPath("commentId").description("삭제한 댓글 번호")
                        )
                ))
                .andExpect(jsonPath("$.commentId").value(1L));

    }
}