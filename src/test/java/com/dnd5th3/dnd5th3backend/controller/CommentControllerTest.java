package com.dnd5th3.dnd5th3backend.controller;

import com.dnd5th3.dnd5th3backend.config.MockSecurityFilter;
import com.dnd5th3.dnd5th3backend.controller.dto.comment.CommentRequestDto;
import com.dnd5th3.dnd5th3backend.domain.member.Member;
import com.dnd5th3.dnd5th3backend.repository.MemberRepository;

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

import static com.dnd5th3.dnd5th3backend.utils.ApiDocumentUtils.getDocumentRequest;
import static com.dnd5th3.dnd5th3backend.utils.ApiDocumentUtils.getDocumentResponse;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;

import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
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

    private Member member;

    @BeforeEach
    void setUp(RestDocumentationContextProvider restDocumentation){
        mvc = MockMvcBuilders.webAppContextSetup(context)
                .apply(documentationConfiguration(restDocumentation))
                .apply(springSecurity(new MockSecurityFilter()))
                .build();
        member = memberRepository.findByEmail("test@gmail.com");
    }

    @DisplayName("댓글 조회 API 테스트")
    @Test
    void getAPI() throws Exception {
        long postId = 1;
        ResultActions actions = mvc.perform(RestDocumentationRequestBuilders.get("/api/v1/comment/{postId}?pageNum=0",postId)
                .principal(new UsernamePasswordAuthenticationToken(member,null))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding(Charsets.UTF_8.toString()));

        actions
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("comment/get",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        pathParameters(
                                parameterWithName("postId").description("게시글 ID")
                        ),
                        responseFields(
                                fieldWithPath("commentResponseList.[].commentId").description("댓글 고유 번호"),
                                fieldWithPath("commentResponseList.[].groupNo").description("댓글 그룹번호"),
                                fieldWithPath("commentResponseList.[].commentLayer").description("댓글 계층"),
                                fieldWithPath("commentResponseList.[].commentOrder").description("댓글 순서"),
                                fieldWithPath("commentResponseList.[].commentOrder").description("댓글 순서"),
                                fieldWithPath("commentResponseList.[].content").description("댓글 내용"),
                                fieldWithPath("commentResponseList.[].createdDate").description("생성일자"),
                                fieldWithPath("commentResponseList.[].updatedDate").description("수정일자"),
                                fieldWithPath("commentResponseList.[].emojiList").description("이모지 리스트"),
                                fieldWithPath("commentResponseList.[].emojiList.[].emojiId").description("이모지 ID"),
                                fieldWithPath("commentResponseList.[].emojiList.[].count").description("이모지 개수"),
                                fieldWithPath("commentResponseList.[].emojiList.[].checked").description("유저 클릭 여부 "),
                                fieldWithPath("pageNum").description("페이지 번호"),
                                fieldWithPath("totalPage").description("전체 페이지수"),
                                fieldWithPath("totalCount").description("전체 개수")
                        )
                ))
                .andExpect(jsonPath("$.pageNum").value(0L));

    }

    @DisplayName("댓글 등록 API 테스트")
    @Test
    void saveAPI() throws Exception {

        long postId = 1;
        long groupNo = 8;
        int commentLayer = 0;
        int commentOrder = 0;
        String content = "comment test";

        CommentRequestDto commentRequestDto = new CommentRequestDto(postId, null, groupNo, commentLayer, commentOrder, content);

        ResultActions actions = mvc.perform(RestDocumentationRequestBuilders.post("/api/v1/comment")
                .principal(new UsernamePasswordAuthenticationToken(member,null))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding(Charsets.UTF_8.toString())
                .content(objectMapper.writeValueAsString(commentRequestDto)));

        actions
                .andExpect(status().isCreated())
                .andDo(print())
                .andDo(document("comment/save",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        relaxedRequestFields(
                                fieldWithPath("postId").description("글 번호"),
                                fieldWithPath("groupNo").description("댓글 그룹번호"),
                                fieldWithPath("commentLayer").description("댓글 계층"),
                                fieldWithPath("commentOrder").description("댓글 순서"),
                                fieldWithPath("content").description("댓글 내용")
                        ),
                        responseFields(
                                fieldWithPath("commentId").description("댓글 번호"),
                                fieldWithPath("groupNo").description("댓글 그룹번호"),
                                fieldWithPath("commentLayer").description("댓글 계층"),
                                fieldWithPath("commentOrder").description("댓글 순서"),
                                fieldWithPath("content").description("댓글 내용"),
                                fieldWithPath("createdDate").description("생성일자"),
                                fieldWithPath("updatedDate").description("수정일자"),
                                fieldWithPath("isDeleted").description("삭제 여부")
                        )
                ))
                .andExpect(jsonPath("$.commentId").value(7L));

    }

    @DisplayName("댓글 수정 API 테스트")
    @Test
    void editAPI() throws Exception {

        CommentRequestDto commentRequestDto = new CommentRequestDto(null, 1L, null, null, null, "comment edit");

        ResultActions actions = mvc.perform(RestDocumentationRequestBuilders.put("/api/v1/comment")
                .principal(new UsernamePasswordAuthenticationToken(member,null))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding(Charsets.UTF_8.toString())
                .content(objectMapper.writeValueAsString(commentRequestDto)));

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
                                fieldWithPath("commentId").description("댓글 번호"),
                                fieldWithPath("groupNo").description("댓글 그룹번호"),
                                fieldWithPath("commentLayer").description("댓글 계층"),
                                fieldWithPath("commentOrder").description("댓글 순서"),
                                fieldWithPath("content").description("댓글 내용"),
                                fieldWithPath("createdDate").description("생성일자"),
                                fieldWithPath("updatedDate").description("수정일자"),
                                fieldWithPath("isDeleted").description("삭제 여부")
                        )
                ))
                .andExpect(jsonPath("$.commentId").value(1L));

    }

    @DisplayName("댓글 삭제 API 테스트")
    @Test
    void deleteAPI() throws Exception {

        CommentRequestDto commentRequestDto = new CommentRequestDto(null, 1L, null, null, null, null);

        ResultActions actions = mvc.perform(RestDocumentationRequestBuilders.delete("/api/v1/comment")
                .principal(new UsernamePasswordAuthenticationToken(member,null))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding(Charsets.UTF_8.toString())
                .content(objectMapper.writeValueAsString(commentRequestDto)));

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
                                fieldWithPath("commentId").description("삭제된 댓글 번호"),
                                fieldWithPath("groupNo").description("댓글 그룹번호"),
                                fieldWithPath("commentLayer").description("댓글 계층"),
                                fieldWithPath("commentOrder").description("댓글 순서"),
                                fieldWithPath("content").description("댓글 내용"),
                                fieldWithPath("createdDate").description("생성일자"),
                                fieldWithPath("updatedDate").description("수정일자"),
                                fieldWithPath("isDeleted").description("삭제 여부")
                        )
                ))
                .andExpect(jsonPath("$.commentId").value(1L));

    }
}