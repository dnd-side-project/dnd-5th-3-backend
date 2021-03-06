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

    @DisplayName("?????? ?????? API ?????????")
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
                                parameterWithName("postId").description("??? ID")
                        ),
                        requestParameters(
                                parameterWithName("pageNum").description("????????? ??????")
                        ),
                        responseFields(
                                fieldWithPath("commentList.[].commentId").description("?????? ID"),
                                fieldWithPath("commentList.[].memberId").description("?????? ID"),
                                fieldWithPath("commentList.[].email").description("?????? ?????????"),
                                fieldWithPath("commentList.[].writerName").description("????????? ?????????"),
                                fieldWithPath("commentList.[].commentLayer").description("?????? ??????"),
                                fieldWithPath("commentList.[].content").description("?????? ??????"),
                                fieldWithPath("commentList.[].voteType").description("?????? ??????"),
                                fieldWithPath("commentList.[].createdDate").description("????????????"),
                                fieldWithPath("commentList.[].updatedDate").description("????????????"),
                                fieldWithPath("commentList.[].replyCount").description("????????? ??????"),
                                fieldWithPath("commentList.[].deleted").description("????????????"),
                                fieldWithPath("commentList.[].emojiList").description("?????? ????????? ?????????"),
                                fieldWithPath("commentList.[].emojiList.[].commentEmojiId").description("??????????????? ID"),
                                fieldWithPath("commentList.[].emojiList.[].emojiId").description("????????? ID"),
                                fieldWithPath("commentList.[].emojiList.[].emojiCount").description("????????? ??????"),
                                fieldWithPath("commentList.[].emojiList.[].checked").description("?????? ?????? ?????? "),
                                fieldWithPath("pageNum").description("????????? ??????"),
                                fieldWithPath("totalPage").description("?????? ????????????"),
                                fieldWithPath("totalCount").description("?????? ??????")
                        )
                ))
                .andExpect(jsonPath("$.totalCount").value(3L));

    }

    @DisplayName("?????? ???????????? API ?????????")
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
                                parameterWithName("commentId").description("?????? ID")
                        ),
                        relaxedResponseFields(
                                fieldWithPath("commentList.[].commentId").description("?????? ID"),
                                fieldWithPath("commentList.[].memberId").description("?????? ID"),
                                fieldWithPath("commentList.[].email").description("?????? ?????????"),
                                fieldWithPath("commentList.[].writerName").description("????????? ?????????"),
                                fieldWithPath("commentList.[].commentLayer").description("?????? ??????"),
                                fieldWithPath("commentList.[].content").description("?????? ??????"),
                                fieldWithPath("commentList.[].voteType").description("?????? ??????"),
                                fieldWithPath("commentList.[].createdDate").description("????????????"),
                                fieldWithPath("commentList.[].updatedDate").description("????????????"),
                                fieldWithPath("commentList.[].deleted").description("????????????"),
                                fieldWithPath("totalCount").description("?????? ?????? ??????(?????? ??????)")
                        )
                ))
                .andExpect(jsonPath("$.totalCount").value(2L));

    }

    @DisplayName("?????? ?????? API ?????????")
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
                                fieldWithPath("postId").description("??? ID"),
                                fieldWithPath("commentLayer").description("?????? ??????"),
                                fieldWithPath("content").description("?????? ??????")
                        ),
                        responseFields(
                                fieldWithPath("commentId").description("????????? ?????? ID"),
                                fieldWithPath("commentLayer").description("?????? ??????"),
                                fieldWithPath("content").description("?????? ??????"),
                                fieldWithPath("createdDate").description("????????????"),
                                fieldWithPath("updatedDate").description("????????????"),
                                fieldWithPath("isDeleted").description("?????? ??????")
                        )
                ))
                .andExpect(jsonPath("$.commentId").value(6L));

    }

    @DisplayName("????????? ?????? API ?????????")
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
                                parameterWithName("commentId").description("?????? ID")
                        ),
                        relaxedRequestFields(
                                fieldWithPath("commentLayer").description("?????? ??????"),
                                fieldWithPath("content").description("?????? ??????")
                        ),
                        responseFields(
                                fieldWithPath("commentId").description("????????? ?????? ID"),
                                fieldWithPath("commentLayer").description("?????? ??????"),
                                fieldWithPath("content").description("?????? ??????"),
                                fieldWithPath("createdDate").description("????????????"),
                                fieldWithPath("updatedDate").description("????????????"),
                                fieldWithPath("isDeleted").description("?????? ??????")
                        )
                ))
                .andExpect(jsonPath("$.content").value(content));

    }

    @DisplayName("?????? ?????? API ?????????")
    @Order(5)
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
                                fieldWithPath("commentId").description("?????? ID"),
                                fieldWithPath("content").description("????????? ?????? ??????")
                        ),
                        relaxedResponseFields(
                                fieldWithPath("commentId").description("?????? ID"),
                                fieldWithPath("commentLayer").description("?????? ??????"),
                                fieldWithPath("content").description("????????? ?????? ??????"),
                                fieldWithPath("createdDate").description("????????????"),
                                fieldWithPath("updatedDate").description("????????????"),
                                fieldWithPath("isDeleted").description("?????? ??????")
                        )
                ))
                .andExpect(jsonPath("$.commentId").value(commentId));

    }

    @DisplayName("?????? ?????? API ?????????")
    @Order(6)
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
                                fieldWithPath("commentId").description("????????? ?????? ID")
                        ),
                        relaxedResponseFields(
                                fieldWithPath("commentId").description("????????? ?????? ID"),
                                fieldWithPath("commentLayer").description("?????? ??????"),
                                fieldWithPath("content").description("?????? ??????"),
                                fieldWithPath("createdDate").description("????????????"),
                                fieldWithPath("updatedDate").description("????????????"),
                                fieldWithPath("isDeleted").description("?????? ??????")
                        )
                ))
                .andExpect(jsonPath("$.commentId").value(commentId))
                .andExpect(jsonPath("$.isDeleted").value(true));

    }
}