package com.dnd5th3.dnd5th3backend.controller;

import com.dnd5th3.dnd5th3backend.config.MockSecurityFilter;
import com.dnd5th3.dnd5th3backend.domain.member.Member;
import com.dnd5th3.dnd5th3backend.repository.member.MemberRepository;
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

import static com.dnd5th3.dnd5th3backend.utils.ApiDocumentUtils.getDocumentRequest;
import static com.dnd5th3.dnd5th3backend.utils.ApiDocumentUtils.getDocumentResponse;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
@TestMethodOrder(value = MethodOrderer.OrderAnnotation.class)
@ActiveProfiles("h2")
class NoticeControllerTest {

    private MockMvc mvc;

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


    @DisplayName("공지사항 목록 조회 API")
    @Test
    void getNoticeListAPI() throws Exception {
        ResultActions actions = mvc.perform(RestDocumentationRequestBuilders.get("/api/v1/notice")
                .principal(new UsernamePasswordAuthenticationToken(member,null))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding(Charsets.UTF_8.toString()));

        actions
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("notice/get",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        responseFields(
                                fieldWithPath("noticeList.[].noticeId").description("공지사항 ID"),
                                fieldWithPath("noticeList.[].title").description("공지사항 제목"),
                                fieldWithPath("noticeList.[].content").description("공지사항 내용"),
                                fieldWithPath("noticeList.[].createdDate").description("생성일자"),
                                fieldWithPath("noticeList.[].updatedDate").description("수정일자")
                        )
                ))
                .andExpect(jsonPath("$.noticeList[0].noticeId").value(2L));
    }

    @DisplayName("공지사항 상세 조회 API")
    @Test
    void getNoticeAPI() throws Exception {

        long noticeId = 1;
        ResultActions actions = mvc.perform(RestDocumentationRequestBuilders.get("/api/v1/notice/{noticeId}",noticeId)
                .principal(new UsernamePasswordAuthenticationToken(member,null))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding(Charsets.UTF_8.toString()));

        actions
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("notice/detail",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        pathParameters(
                                parameterWithName("noticeId").description("공지사항 ID")
                        ),
                        responseFields(
                                fieldWithPath("noticeId").description("공지사항 ID"),
                                fieldWithPath("title").description("공지사항 제목"),
                                fieldWithPath("content").description("공지사항 내용"),
                                fieldWithPath("createdDate").description("생성일자"),
                                fieldWithPath("updatedDate").description("수정일자")
                        )
                ))
                .andExpect(jsonPath("$.noticeId").value(1L));
    }
}