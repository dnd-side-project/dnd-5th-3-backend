package com.dnd5th3.dnd5th3backend.controller;

import com.dnd5th3.dnd5th3backend.config.MockSecurityFilter;
import com.dnd5th3.dnd5th3backend.domain.member.Member;
import com.dnd5th3.dnd5th3backend.repository.member.MemberRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
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
import static org.junit.jupiter.api.Assertions.*;
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
@TestMethodOrder(value = MethodOrderer.OrderAnnotation.class)
@ActiveProfiles("h2")
class EmojiControllerTest {

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

    @DisplayName("댓글의 이모지 등록 API 테스트")
    @Test
    void saveAPI() throws Exception {
        long commentId = 1;
        long emojiId = 1;

        Map<String,Object> request = new HashMap<>();
        request.put("commentId",commentId);
        request.put("emojiId",emojiId);
        
        ResultActions actions = mvc.perform(RestDocumentationRequestBuilders.post("/api/v1/emoji")
                .principal(new UsernamePasswordAuthenticationToken(member,null))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding(Charsets.UTF_8.toString())
                .content(objectMapper.writeValueAsString(request)));

        actions
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("emoji/save",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        requestFields(
                                fieldWithPath("commentId").description("댓글 ID"),
                                fieldWithPath("emojiId").description("이모지 ID")
                        ),
                        responseFields(
                                fieldWithPath("commentEmojiId").description("생성된 댓글이모지 ID"),
                                fieldWithPath("commentEmojiCount").description("댓글이모지 개수")
                        )
                ))
                .andExpect(jsonPath("$.commentEmojiId").value(6L))
                .andExpect(jsonPath("$.commentEmojiCount").value(1));
    }

    @DisplayName("댓글의 이모지 업데이트 API 테스트")
    @Test
    void updateAPI() throws Exception {

        long commentEmojiId = 3;
        boolean isChecked = true;

        Map<String,Object> request = new HashMap<>();
        request.put("commentEmojiId",commentEmojiId);
        request.put("isChecked",isChecked);

        ResultActions actions = mvc.perform(RestDocumentationRequestBuilders.put("/api/v1/emoji")
                .principal(new UsernamePasswordAuthenticationToken(member,null))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding(Charsets.UTF_8.toString())
                .content(objectMapper.writeValueAsString(request)));

        actions
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("emoji/update",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        requestFields(
                                fieldWithPath("commentEmojiId").description("댓글 ID"),
                                fieldWithPath("isChecked").description("체크 여부")
                        ),
                        responseFields(
                                fieldWithPath("commentEmojiId").description("업데이트한 댓글이모지 ID"),
                                fieldWithPath("commentEmojiCount").description("업데이트된 댓글이모지 개수")
                        )
                ))
                .andExpect(jsonPath("$.commentEmojiId").value(commentEmojiId))
                .andExpect(jsonPath("$.commentEmojiCount").value(2));
    }
}