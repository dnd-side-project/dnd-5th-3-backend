package com.dnd5th3.dnd5th3backend.controller;

import com.dnd5th3.dnd5th3backend.config.MockSecurityFilter;
import com.dnd5th3.dnd5th3backend.controller.dto.comment.CommentRequestDto;
import com.dnd5th3.dnd5th3backend.domain.member.Member;

import com.dnd5th3.dnd5th3backend.repository.member.MemberRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Charsets;

import org.junit.jupiter.api.*;
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

import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
@TestMethodOrder(value = MethodOrderer.OrderAnnotation.class)
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
    @Order(1)
    @Test
    void getAllAPI() throws Exception {
        long postId = 1;
        ResultActions actions = mvc.perform(RestDocumentationRequestBuilders.get("/api/v1/comment/{postId}/posts?pageNum=0",postId)
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
                                parameterWithName("postId").description("글 ID")
                        ),
                        requestParameters(
                                parameterWithName("pageNum").description("페이지 번호")
                        ),
                        responseFields(
                                fieldWithPath("commentList.[].commentId").description("댓글 ID"),
                                fieldWithPath("commentList.[].memberId").description("회원 ID"),
                                fieldWithPath("commentList.[].email").description("회원 이메일"),
                                fieldWithPath("commentList.[].writerName").description("작성자 닉네임"),
                                fieldWithPath("commentList.[].commentLayer").description("댓글 계층"),
                                fieldWithPath("commentList.[].content").description("댓글 내용"),
                                fieldWithPath("commentList.[].voteType").description("투표 상태"),
                                fieldWithPath("commentList.[].createdDate").description("생성일자"),
                                fieldWithPath("commentList.[].updatedDate").description("수정일자"),
                                fieldWithPath("commentList.[].replyCount").description("대댓글 개수"),
                                fieldWithPath("commentList.[].deleted").description("삭제여부"),
                                fieldWithPath("commentList.[].emojiList").description("댓글 이모지 리스트"),
                                fieldWithPath("commentList.[].emojiList.[].emojiId").description("이모지 ID"),
                                fieldWithPath("commentList.[].emojiList.[].emojiCount").description("이모지 개수"),
                                fieldWithPath("commentList.[].emojiList.[].checked").description("유저 클릭 여부 "),
                                fieldWithPath("pageNum").description("페이지 번호"),
                                fieldWithPath("totalPage").description("전체 페이지수"),
                                fieldWithPath("totalCount").description("전체 개수")
                        )
                ))
                .andExpect(jsonPath("$.totalCount").value(3L));

    }

    @DisplayName("댓글 상세조회 API 테스트")
    @Order(2)
    @Test
    void getDetailAPI() throws Exception {
        long commentId =3;
        ResultActions actions = mvc.perform(RestDocumentationRequestBuilders.get("/api/v1/comment/{commentId}",commentId)
                .principal(new UsernamePasswordAuthenticationToken(member,null))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding(Charsets.UTF_8.toString()));

        actions
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("comment/detail",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        pathParameters(
                                parameterWithName("commentId").description("댓글 ID")
                        ),
                        relaxedResponseFields(
                                fieldWithPath("commentList.[].commentId").description("댓글 ID"),
                                fieldWithPath("commentList.[].memberId").description("회원 ID"),
                                fieldWithPath("commentList.[].email").description("회원 이메일"),
                                fieldWithPath("commentList.[].writerName").description("작성자 닉네임"),
                                fieldWithPath("commentList.[].commentLayer").description("댓글 계층"),
                                fieldWithPath("commentList.[].content").description("댓글 내용"),
                                fieldWithPath("commentList.[].voteType").description("투표 상태"),
                                fieldWithPath("commentList.[].createdDate").description("생성일자"),
                                fieldWithPath("commentList.[].updatedDate").description("수정일자"),
                                fieldWithPath("commentList.[].deleted").description("삭제여부"),
                                fieldWithPath("totalCount").description("댓글 전체 개수(상단 포함)")
                        )
                ))
                .andExpect(jsonPath("$.totalCount").value(3L));

    }

    @DisplayName("댓글 등록 API 테스트")
    @Order(3)
    @Test
    void saveAPI() throws Exception {

        long postId = 1;
        int commentLayer = 0;
        String content = "comment save test";

        Map<String,Object> request = new HashMap<>();
        request.put("postId",postId);
        request.put("commentLayer",commentLayer);
        request.put("content",content);

        ResultActions actions = mvc.perform(RestDocumentationRequestBuilders.post("/api/v1/comment")
                .principal(new UsernamePasswordAuthenticationToken(member,null))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding(Charsets.UTF_8.toString())
                .content(objectMapper.writeValueAsString(request)));

        actions
                .andExpect(status().isCreated())
                .andDo(print())
                .andDo(document("comment/save",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        relaxedRequestFields(
                                fieldWithPath("postId").description("글 ID"),
                                fieldWithPath("commentLayer").description("댓글 계층"),
                                fieldWithPath("content").description("댓글 내용")
                        ),
                        responseFields(
                                fieldWithPath("commentId").description("생성된 댓글 ID"),
                                fieldWithPath("commentLayer").description("댓글 계층"),
                                fieldWithPath("content").description("댓글 내용"),
                                fieldWithPath("createdDate").description("생성일자"),
                                fieldWithPath("updatedDate").description("수정일자"),
                                fieldWithPath("isDeleted").description("삭제 여부")
                        )
                ))
                .andExpect(jsonPath("$.commentId").value(6L));

    }

    @DisplayName("대댓글 등록 API 테스트")
    @Order(4)
    @Test
    void saveReplyAPI() throws Exception {

        long commentId = 5;
        int commentLayer = 1;
        String content = "comment reply save test";

        Map<String,Object> request = new HashMap<>();
        request.put("commentLayer",commentLayer);
        request.put("content",content);

        ResultActions actions = mvc.perform(RestDocumentationRequestBuilders.post("/api/v1/comment/{commentId}/reply",commentId)
                .principal(new UsernamePasswordAuthenticationToken(member,null))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding(Charsets.UTF_8.toString())
                .content(objectMapper.writeValueAsString(request)));

        actions
                .andExpect(status().isCreated())
                .andDo(print())
                .andDo(document("comment/reply",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        pathParameters(
                                parameterWithName("commentId").description("댓글 ID")
                        ),
                        relaxedRequestFields(
                                fieldWithPath("commentLayer").description("댓글 계층"),
                                fieldWithPath("content").description("댓글 내용")
                        ),
                        responseFields(
                                fieldWithPath("commentId").description("생성된 댓글 ID"),
                                fieldWithPath("commentLayer").description("댓글 계층"),
                                fieldWithPath("content").description("댓글 내용"),
                                fieldWithPath("createdDate").description("생성일자"),
                                fieldWithPath("updatedDate").description("수정일자"),
                                fieldWithPath("isDeleted").description("삭제 여부")
                        )
                ))
                .andExpect(jsonPath("$.content").value(content));

    }

    @DisplayName("댓글 수정 API 테스트")
    @Test
    void editAPI() throws Exception {

        long commentId = 1;
        String content = "comment edit test";

        Map<String,Object> request = new HashMap<>();
        request.put("commentId",commentId);
        request.put("content",content);

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
                                fieldWithPath("commentId").description("댓글 ID"),
                                fieldWithPath("content").description("수정할 댓글 내용")
                        ),
                        relaxedResponseFields(
                                fieldWithPath("commentId").description("댓글 ID"),
                                fieldWithPath("commentLayer").description("댓글 계층"),
                                fieldWithPath("content").description("수정된 댓글 내용"),
                                fieldWithPath("createdDate").description("생성일자"),
                                fieldWithPath("updatedDate").description("수정일자"),
                                fieldWithPath("isDeleted").description("삭제 여부")
                        )
                ))
                .andExpect(jsonPath("$.commentId").value(commentId));

    }

    @DisplayName("댓글 삭제 API 테스트")
    @Test
    void deleteAPI() throws Exception {
        long commentId = 1;

        Map<String,Object> request = new HashMap<>();
        request.put("commentId",commentId);

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
                                fieldWithPath("commentId").description("삭제할 댓글 ID")
                        ),
                        relaxedResponseFields(
                                fieldWithPath("commentId").description("삭제된 댓글 ID"),
                                fieldWithPath("commentLayer").description("댓글 계층"),
                                fieldWithPath("content").description("댓글 내용"),
                                fieldWithPath("createdDate").description("생성일자"),
                                fieldWithPath("updatedDate").description("수정일자"),
                                fieldWithPath("isDeleted").description("삭제 여부")
                        )
                ))
                .andExpect(jsonPath("$.commentId").value(commentId))
                .andExpect(jsonPath("$.isDeleted").value(true));

    }
}