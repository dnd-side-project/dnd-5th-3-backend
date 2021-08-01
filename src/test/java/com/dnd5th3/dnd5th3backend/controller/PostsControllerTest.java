package com.dnd5th3.dnd5th3backend.controller;

import com.dnd5th3.dnd5th3backend.controller.dto.post.SaveRequestDto;
import com.dnd5th3.dnd5th3backend.domain.member.Member;
import com.dnd5th3.dnd5th3backend.domain.member.Role;
import com.dnd5th3.dnd5th3backend.domain.posts.Posts;
import com.dnd5th3.dnd5th3backend.service.MemberService;
import com.dnd5th3.dnd5th3backend.service.PostsService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static com.dnd5th3.dnd5th3backend.utils.ApiDocumentUtils.getDocumentRequest;
import static com.dnd5th3.dnd5th3backend.utils.ApiDocumentUtils.getDocumentResponse;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@AutoConfigureRestDocs
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
public class PostsControllerTest {

    @Autowired
    private MockMvc mvc;
    @MockBean
    private MemberService memberService;
    @MockBean
    private PostsService postsService;
    @Autowired
    private ObjectMapper objectMapper;

    private Member member;

    @BeforeEach
    public void setUp() {
        member = Member.builder().id(1L).email("test@gmail.com").password("1234").role(Role.ROLE_USER).name("name").build();
    }

    @DisplayName("post 생성 api 테스트")
    @Test
    public void savePostApiTest() throws Exception {
        //given
        SaveRequestDto requestDto = SaveRequestDto.builder()
                .memberId(1L)
                .title("test")
                .productName("testProduct")
                .content("test content")
                .productImageUrl("test.jpg")
                .build();
        Posts response = Posts.builder()
                .id(1L)
                .member(member)
                .title("test")
                .productName("testProduct")
                .content("test content")
                .productImageUrl("test.jpg")
                .build();

        given(memberService.findMemberById(requestDto.getMemberId())).willReturn(member);
        given(postsService.savePost(member, requestDto.getTitle(), requestDto.getProductName(), requestDto.getContent(), requestDto.getProductImageUrl())).willReturn(response);

        //when
        ResultActions result = mvc.perform(RestDocumentationRequestBuilders.post("/api/v1/posts")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .content(objectMapper.writeValueAsString(requestDto))
        );

        //then
        result
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("posts/save",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        requestFields(
                                fieldWithPath("memberId").description("사용자 id"),
                                fieldWithPath("title").description("글 제목"),
                                fieldWithPath("productName").description("상품 이름"),
                                fieldWithPath("content").description("글 내용"),
                                fieldWithPath("productImageUrl").description("상품 이미지")
                        ),
                        responseFields(
                                fieldWithPath("id").description("게시글 id").type(Long.class)
                        )
                ))
                .andExpect(jsonPath("$.id").value(1L));
    }
}