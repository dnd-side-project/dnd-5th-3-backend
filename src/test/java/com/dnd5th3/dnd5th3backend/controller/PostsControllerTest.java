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

import java.time.LocalDateTime;
import java.util.Optional;

import static com.dnd5th3.dnd5th3backend.utils.ApiDocumentUtils.getDocumentRequest;
import static com.dnd5th3.dnd5th3backend.utils.ApiDocumentUtils.getDocumentResponse;
import static org.assertj.core.internal.bytebuddy.matcher.ElementMatchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
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

    @DisplayName("post 상세조회 api 테스트")
    @Test
    public void findPostByIdApiTest() throws Exception {
        //given
        Posts post = Posts.builder()
                .id(1L)
                .member(member)
                .title("test")
                .productName("test product")
                .content("content")
                .productImageUrl("test.jpg")
                .isVoted(false)
                .permitCount(2)
                .rejectCount(8)
                .viewCount(100)
                .isDeleted(false)
                .build();
        post.setCreatedDate(LocalDateTime.of(2021, 8, 1, 12, 0, 0));

        given(postsService.findPostById(1L)).willReturn(post);

        //when
        ResultActions result = mvc.perform(RestDocumentationRequestBuilders.get("/api/v1/posts/{id}", 1L));

        //then
        result
                .andDo(print())
                .andDo(document("posts/getById",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        pathParameters(
                                parameterWithName("id").description("게시글 id")
                        ),
                        responseFields(
                                fieldWithPath("name").description("작성자 이름"),
                                fieldWithPath("title").description("글 제목"),
                                fieldWithPath("productName").description("상품 이름"),
                                fieldWithPath("content").description("글 내용"),
                                fieldWithPath("productImageUrl").description("상품 이미지"),
                                fieldWithPath("isVoted").description("투표 종료 여부"),
                                fieldWithPath("permitRatio").description("찬성 투표 비율"),
                                fieldWithPath("rejectRatio").description("반대 투표 비율"),
                                fieldWithPath("createdDate").description("작성된 시간")
                        )
                ))
                .andExpect(jsonPath("$.name").value("name"))
                .andExpect(jsonPath("$.title").value("test"))
                .andExpect(jsonPath("$.productName").value("test product"))
                .andExpect(jsonPath("$.content").value("content"))
                .andExpect(jsonPath("$.productImageUrl").value("test.jpg"))
                .andExpect(jsonPath("$.isVoted").value(false))
                .andExpect(jsonPath("$.permitRatio").value(20L))
                .andExpect(jsonPath("$.rejectRatio").value(80L))
                .andExpect(jsonPath("$.createdDate").value("2021-08-01T12:00:00"));

    }
}