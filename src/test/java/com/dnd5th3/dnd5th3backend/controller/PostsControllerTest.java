package com.dnd5th3.dnd5th3backend.controller;

import com.dnd5th3.dnd5th3backend.config.MockSecurityFilter;
import com.dnd5th3.dnd5th3backend.controller.dto.post.*;
import com.dnd5th3.dnd5th3backend.domain.member.Member;
import com.dnd5th3.dnd5th3backend.domain.member.Role;
import com.dnd5th3.dnd5th3backend.domain.posts.Posts;
import com.dnd5th3.dnd5th3backend.domain.vote.Vote;
import com.dnd5th3.dnd5th3backend.domain.vote.VoteType;
import com.dnd5th3.dnd5th3backend.service.PostsService;
import com.dnd5th3.dnd5th3backend.service.VoteService;
import com.dnd5th3.dnd5th3backend.utils.S3Uploader;
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
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.mock.web.MockPart;
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

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.dnd5th3.dnd5th3backend.utils.ApiDocumentUtils.getDocumentRequest;
import static com.dnd5th3.dnd5th3backend.utils.ApiDocumentUtils.getDocumentResponse;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@AutoConfigureRestDocs
@ExtendWith({SpringExtension.class, RestDocumentationExtension.class})
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@ActiveProfiles("env")
class PostsControllerTest {

    @Autowired
    private MockMvc mvc;
    @Autowired
    private WebApplicationContext context;
    @MockBean
    private PostsService postsService;
    @MockBean
    private VoteService voteService;
    @MockBean
    private S3Uploader s3Uploader;
    @Autowired
    private ObjectMapper objectMapper;

    private Member member;

    @BeforeEach
    void setUp(RestDocumentationContextProvider restDocumentation) {
        mvc = MockMvcBuilders.webAppContextSetup(context)
                .apply(documentationConfiguration(restDocumentation))
                .apply(springSecurity(new MockSecurityFilter()))
                .build();
        member = Member.builder().email("test@gmail.com")
                .password("1234")
                .role(Role.ROLE_USER)
                .name("name")
                .build();
    }

    @DisplayName("post ?????? api ?????????")
    @Test
    void savePostApiTest() throws Exception {
        //given
        String title = "test";
        String content = "content";
        String productImageUrl = "test.jpg";
        MockMultipartFile image = new MockMultipartFile("file", "test.jpg", MediaType.IMAGE_JPEG_VALUE, "test.jpg".getBytes());
        Posts response = Posts.builder()
                .id(1L)
                .member(member)
                .title("test")
                .content("content")
                .productImageUrl("test.jpg")
                .build();
        given(s3Uploader.upload(image, "static")).willReturn(productImageUrl);
        given(postsService.savePost(member, title, content, productImageUrl)).willReturn(response);

        //when
        ResultActions result = mvc.perform(RestDocumentationRequestBuilders.fileUpload("/api/v1/posts")
                .file(image)
                .part(new MockPart("title", "test".getBytes(StandardCharsets.UTF_8)))
                .part(new MockPart("content", "content".getBytes(StandardCharsets.UTF_8)))
                .principal(new UsernamePasswordAuthenticationToken(member, null))
                .header("Authorization", "Bearer Token")
                .contentType(MediaType.MULTIPART_MIXED)
        );

        //then
        result
                .andDo(print())
                .andExpect(status().isCreated())
                .andDo(document("posts/save",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        requestHeaders(
                                headerWithName("Authorization").description("?????? ????????? ??????")
                        ),
                        requestParts(
                                partWithName("title").description("??? ??????"),
                                partWithName("content").description("??? ??????"),
                                partWithName("file").description("?????? ?????????")
                        ),
                        responseFields(
                                fieldWithPath("id").description("????????? ????????? id").type(Long.class)
                        )
                ))
                .andExpect(jsonPath("$.id").value(1L));
    }

    @DisplayName("post ???????????? api ?????????")
    @Test
     void findPostByIdApiTest() throws Exception {
        //given
        Posts post = Posts.builder()
                .id(1L)
                .member(member)
                .title("test")
                .content("content")
                .productImageUrl("test.jpg")
                .isVoted(false)
                .isPostsEnd(false)
                .permitCount(2)
                .rejectCount(8)
                .rankCount(100)
                .voteDeadline(LocalDateTime.of(2021, 8, 2, 12, 0, 0))
                .postsDeadline(LocalDateTime.of(2021, 8, 9, 12, 0, 0))
                .build();
        post.setCreatedDate(LocalDateTime.of(2021, 8, 1, 12, 0, 0));
        Vote vote = Vote.builder().id(1L).member(member).posts(post).result(VoteType.NO_RESULT).build();

        given(postsService.findPostById(1L)).willReturn(post);
        given(voteService.getVoteResult(member, post)).willReturn(vote);

        //when
        ResultActions result = mvc.perform(RestDocumentationRequestBuilders.get("/api/v1/posts/{id}", 1L));

        //then
        result
                .andDo(print())
                .andDo(document("posts/getById",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        pathParameters(
                                parameterWithName("id").description("????????? id")
                        ),
                        responseFields(
                                fieldWithPath("id").description("????????? id"),
                                fieldWithPath("name").description("????????? ??????"),
                                fieldWithPath("title").description("??? ??????"),
                                fieldWithPath("content").description("??? ??????"),
                                fieldWithPath("productImageUrl").description("?????? ?????????"),
                                fieldWithPath("isVoted").description("?????? ?????? ??????"),
                                fieldWithPath("permitCount").description("?????? ?????? ???"),
                                fieldWithPath("rejectCount").description("?????? ?????? ???"),
                                fieldWithPath("createdDate").description("????????? ??????"),
                                fieldWithPath("voteDeadline").description("?????? ?????? ??????"),
                                fieldWithPath("currentMemberVoteResult").description("?????? ???????????? ?????? ??????")
                        )
                ))
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("name"))
                .andExpect(jsonPath("$.title").value("test"))
                .andExpect(jsonPath("$.content").value("content"))
                .andExpect(jsonPath("$.productImageUrl").value("test.jpg"))
                .andExpect(jsonPath("$.isVoted").value(false))
                .andExpect(jsonPath("$.permitCount").value(2))
                .andExpect(jsonPath("$.rejectCount").value(8))
                .andExpect(jsonPath("$.createdDate").value("2021-08-01T12:00:00"))
                .andExpect(jsonPath("$.voteDeadline").value("2021-08-02T12:00:00"))
                .andExpect(jsonPath("$.currentMemberVoteResult").value("NO_RESULT"));
    }

    @DisplayName("post ?????? api ?????????")
    @Test
    void updatePostApiTest() throws Exception {
        //given
        Posts response = Posts.builder()
                .id(1L)
                .member(member)
                .title("update")
                .content("update content")
                .productImageUrl("update.jpg")
                .isVoted(false)
                .isPostsEnd(false)
                .permitCount(36)
                .rejectCount(25)
                .rankCount(70)
                .voteDeadline(LocalDateTime.of(2021, 8, 3, 12, 0, 0))
                .postsDeadline(LocalDateTime.of(2021, 8, 10, 12, 0, 0))
                .build();
        response.setCreatedDate(LocalDateTime.of(2021, 8, 2, 12, 0, 0));
        String title = "update";
        String content = "update content";
        String productImageUrl = "update.jpg";
        MockMultipartFile image = new MockMultipartFile("file", "test.jpg", MediaType.IMAGE_JPEG_VALUE, "test.jpg".getBytes());

        given(s3Uploader.upload(image, "static")).willReturn(productImageUrl);
        given(postsService.updatePost(1L, title, content, productImageUrl)).willReturn(response);

        //when
        ResultActions result = mvc.perform(RestDocumentationRequestBuilders.fileUpload("/api/v1/posts/{id}", 1L)
                .file(image)
                .part(new MockPart("title", "update".getBytes(StandardCharsets.UTF_8)))
                .part(new MockPart("content", "update content".getBytes(StandardCharsets.UTF_8)))
                .contentType(MediaType.MULTIPART_MIXED)
        );

        //then
        result
                .andDo(print())
                .andDo(document("posts/update",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        pathParameters(
                                parameterWithName("id").description("????????? id")
                        ),
                        requestParts(
                                partWithName("title").description("????????? ??????"),
                                partWithName("content").description("????????? ??????"),
                                partWithName("file").description("????????? ?????? ?????????")
                        ),
                        responseFields(
                                fieldWithPath("id").description("????????? ????????? id").type(Long.class)
                        )
                ))
                .andExpect(jsonPath("$.id").value(1L));
    }

    @DisplayName("post ?????? api ?????????")
    @Test
    void deletePostApiTest() throws Exception {
        //when
        ResultActions result = mvc.perform(RestDocumentationRequestBuilders.delete("/api/v1/posts/{id}", 1L)
                .principal(new UsernamePasswordAuthenticationToken(member, null))
                .header("Authorization", "Bearer Token")
        );

        //then
        result
                .andDo(print())
                .andDo(document("posts/delete",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        pathParameters(
                                parameterWithName("id").description("????????? id")
                        ),
                        requestHeaders(
                                headerWithName("Authorization").description("?????? ????????? ??????")
                        ),
                        responseFields(
                                fieldWithPath("id").description("????????? ????????? id").type(Long.class)
                        )
                ))
                .andExpect(jsonPath("$.id").value(1L));
    }

    @DisplayName("?????? ????????? ?????? api ?????????")
    @Test
    void findPostsListApiTest() throws Exception {
        //given
        List<PostsListDto> listDtos = new ArrayList<>();
        PostsListDto dto1 = PostsListDto.builder()
                .id(1L)
                .name(member.getName())
                .title("test1")
                .productImageUrl("test1.jpg")
                .isVoted(false)
                .permitRatio(29L)
                .rejectRatio(71L)
                .createdDate(LocalDateTime.of(2021, 8, 4, 12, 0, 0))
                .voteDeadline(LocalDateTime.of(2021, 8, 5, 12, 0, 0))
                .build();
        PostsListDto dto2 = PostsListDto.builder()
                .id(2L)
                .name(member.getName())
                .title("test2").
                productImageUrl("test2.jpg")
                .isVoted(false)
                .permitRatio(75L)
                .rejectRatio(25L)
                .createdDate(LocalDateTime.of(2021, 8, 4, 15, 0, 0))
                .voteDeadline(LocalDateTime.of(2021, 8, 5, 15, 0, 0))
                .build();
        listDtos.add(dto2);
        listDtos.add(dto1);
        AllPostResponseDto responseDto = AllPostResponseDto.builder().listDtos(listDtos).build();

        given(postsService.findAllPostsWithSortType(SortType.CREATED_DATE.getValue())).willReturn(responseDto);

        //when
        ResultActions rankCountResult = mvc.perform(RestDocumentationRequestBuilders.get("/api/v1/posts?sorted=rank-count"));
        ResultActions createdDateResult = mvc.perform(RestDocumentationRequestBuilders.get("/api/v1/posts?sorted=created-date"));
        ResultActions alreadyDoneResult = mvc.perform(RestDocumentationRequestBuilders.get("/api/v1/posts?sorted=already-done"));
        ResultActions almostDoneResult = mvc.perform(RestDocumentationRequestBuilders.get("/api/v1/posts?sorted=almost-done"));

        //then
        rankCountResult
                .andDo(document("posts/findAll/rankCount",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        requestParameters(
                                parameterWithName("sorted").description("?????? ??????")
                        )
                ))
                .andExpect(status().isOk());

        createdDateResult
                .andDo(document("posts/findAll/createdDate",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        requestParameters(
                                parameterWithName("sorted").description("?????? ??????")
                        ),
                        responseFields(
                                fieldWithPath("listDtos.[].id").description("????????? id"),
                                fieldWithPath("listDtos.[].name").description("????????? ??????"),
                                fieldWithPath("listDtos.[].title").description("??? ??????"),
                                fieldWithPath("listDtos.[].productImageUrl").description("?????? ?????????"),
                                fieldWithPath("listDtos.[].isVoted").description("?????? ?????? ??????"),
                                fieldWithPath("listDtos.[].permitRatio").description("?????? ?????? ??????"),
                                fieldWithPath("listDtos.[].rejectRatio").description("?????? ?????? ??????"),
                                fieldWithPath("listDtos.[].createdDate").description("????????? ??????"),
                                fieldWithPath("listDtos.[].voteDeadline").description("?????? ?????? ??????")
                        )
                ))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.listDtos[0].id").value(2L))
                .andExpect(jsonPath("$.listDtos[0].name").value("name"))
                .andExpect(jsonPath("$.listDtos[0].title").value("test2"))
                .andExpect(jsonPath("$.listDtos[0].productImageUrl").value("test2.jpg"))
                .andExpect(jsonPath("$.listDtos[0].isVoted").value(false))
                .andExpect(jsonPath("$.listDtos[0].permitRatio").value(75L))
                .andExpect(jsonPath("$.listDtos[0].rejectRatio").value(25L))
                .andExpect(jsonPath("$.listDtos[0].createdDate").value("2021-08-04T15:00:00"))
                .andExpect(jsonPath("$.listDtos[0].voteDeadline").value("2021-08-05T15:00:00"))
                .andExpect(jsonPath("$.listDtos[1].id").value(1L))
                .andExpect(jsonPath("$.listDtos[1].name").value("name"))
                .andExpect(jsonPath("$.listDtos[1].title").value("test1"))
                .andExpect(jsonPath("$.listDtos[1].productImageUrl").value("test1.jpg"))
                .andExpect(jsonPath("$.listDtos[1].isVoted").value(false))
                .andExpect(jsonPath("$.listDtos[1].permitRatio").value(29L))
                .andExpect(jsonPath("$.listDtos[1].rejectRatio").value(71L))
                .andExpect(jsonPath("$.listDtos[1].createdDate").value("2021-08-04T12:00:00"))
                .andExpect(jsonPath("$.listDtos[1].voteDeadline").value("2021-08-05T12:00:00"));

        alreadyDoneResult
                .andDo(document("posts/findAll/alreadyDone",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        requestParameters(
                                parameterWithName("sorted").description("?????? ??????")
                        )
                ))
                .andExpect(status().isOk());

        almostDoneResult
                .andDo(document("posts/findAll/almostDone",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        requestParameters(
                                parameterWithName("sorted").description("?????? ??????")
                        )
                ))
                .andExpect(status().isOk());
    }

    @DisplayName("?????? ?????? api ?????????")
    @Test
    void votePostApiTest() throws Exception {
        //given
        VoteRequestDto requestDto = new VoteRequestDto(VoteType.PERMIT);
        Posts posts = Posts.builder()
                .id(1L)
                .member(member)
                .title("test")
                .content("test content")
                .productImageUrl("test.jpg")
                .build();
        Vote response = Vote.builder()
                .id(1L)
                .member(member)
                .posts(posts)
                .result(VoteType.PERMIT)
                .build();
        given(postsService.findPostById(1L)).willReturn(posts);
        given(voteService.saveVote(member, posts, requestDto.getResult())).willReturn(response);

        //when
        ResultActions result = mvc.perform(RestDocumentationRequestBuilders.post("/api/v1/posts/{id}/vote", 1L)
                .principal(new UsernamePasswordAuthenticationToken(member, null))
                .header("Authorization", "Bearer Token")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .content(objectMapper.writeValueAsString(requestDto))
        );

        //then
        result
                .andDo(print())
                .andDo(document("posts/vote",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        pathParameters(
                                parameterWithName("id").description("????????? ????????? id")
                        ),
                        requestHeaders(
                                headerWithName("Authorization").description("?????? ????????? ??????")
                        ),
                        requestFields(
                                fieldWithPath("result").description("PERMIT(??????), REJECT(??????)")
                        ),
                        responseFields(
                                fieldWithPath("id").description("????????? ????????? id")
                        )
                ))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @DisplayName("??????????????? post ?????? api ?????????")
    @Test
    void mainPageApiTest() throws Exception {
        //given
        Posts hotPost = Posts.builder()
                .id(1L)
                .member(member)
                .title("????????? ?????? ???")
                .content("test content")
                .productImageUrl("test.jpg")
                .isVoted(false)
                .isPostsEnd(false)
                .permitCount(10)
                .rejectCount(10)
                .rankCount(100)
                .voteDeadline(LocalDateTime.of(2021, 8, 13, 12, 0, 0))
                .postsDeadline(LocalDateTime.of(2021, 8, 20, 12, 0, 0))
                .build();
        hotPost.setCreatedDate(LocalDateTime.of(2021, 8, 12, 12, 0, 0));
        Posts belovedPost = Posts.builder()
                .id(2L)
                .member(member)
                .title("?????? ?????? ?????? ???")
                .content("test content")
                .productImageUrl("test.jpg")
                .isVoted(false)
                .isPostsEnd(false)
                .permitCount(10)
                .rejectCount(10)
                .rankCount(80)
                .voteDeadline(LocalDateTime.of(2021, 8, 13, 12, 0, 0))
                .postsDeadline(LocalDateTime.of(2021, 8, 20, 12, 0, 0))
                .build();
        belovedPost.setCreatedDate(LocalDateTime.of(2021, 8, 12, 12, 0, 0));
        Posts recommendPost = Posts.builder()
                .id(3L)
                .member(member)
                .title("????????? ?????????")
                .content("test content")
                .productImageUrl("test.jpg")
                .isVoted(false)
                .isPostsEnd(false)
                .permitCount(0)
                .rejectCount(0)
                .rankCount(90)
                .voteDeadline(LocalDateTime.of(2021, 8, 13, 12, 0, 0))
                .postsDeadline(LocalDateTime.of(2021, 8, 20, 12, 0, 0))
                .build();
        recommendPost.setCreatedDate(LocalDateTime.of(2021, 8, 12, 12, 0, 0));
        Posts bestResponsePost = Posts.builder()
                .id(4L)
                .member(member)
                .title("????????? ?????????")
                .content("test content")
                .productImageUrl("test.jpg")
                .isVoted(false)
                .isPostsEnd(false)
                .permitCount(80)
                .rejectCount(80)
                .rankCount(120)
                .voteDeadline(LocalDateTime.of(2021, 8, 13, 12, 0, 0))
                .postsDeadline(LocalDateTime.of(2021, 8, 20, 12, 0, 0))
                .build();
        bestResponsePost.setCreatedDate(LocalDateTime.of(2021, 8, 12, 12, 0, 0));
        Posts neckAndNeckPost = Posts.builder()
                .id(5L)
                .member(member)
                .title("???????????? ?????????")
                .content("test content")
                .productImageUrl("test.jpg")
                .isVoted(false)
                .isPostsEnd(false)
                .permitCount(80)
                .rejectCount(80)
                .rankCount(100)
                .voteDeadline(LocalDateTime.of(2021, 8, 13, 12, 0, 0))
                .postsDeadline(LocalDateTime.of(2021, 8, 20, 12, 0, 0))
                .build();
        neckAndNeckPost.setCreatedDate(LocalDateTime.of(2021, 8, 12, 12, 0, 0));
        Map<String, Posts> mainPostsMap = new HashMap<>();
        mainPostsMap.put("hotPost", hotPost);
        mainPostsMap.put("belovedPost", belovedPost);
        mainPostsMap.put("recommendPost", recommendPost);
        mainPostsMap.put("bestResponsePost", bestResponsePost);
        mainPostsMap.put("neckAndNeckPost", neckAndNeckPost);

        given(postsService.findMainPosts()).willReturn(mainPostsMap);

        //when
        ResultActions result = mvc.perform(RestDocumentationRequestBuilders.get("/api/v1/posts/main"));

        //then
        result
                .andDo(print())
                .andDo(document("posts/main",
                        getDocumentRequest(),
                        getDocumentResponse()
                ))
                .andExpect(jsonPath("$.posts[2].voteDeadline").value("2021-08-13T12:00:00"))
                .andExpect(jsonPath("$.posts[0].id").value(3L))
                .andExpect(jsonPath("$.posts[0].name").value("name"))
                .andExpect(jsonPath("$.posts[0].title").value("????????? ?????????"))
                .andExpect(jsonPath("$.posts[0].productImageUrl").value("test.jpg"))
                .andExpect(jsonPath("$.posts[0].isVoted").value(false))
                .andExpect(jsonPath("$.posts[0].permitRatio").value(0))
                .andExpect(jsonPath("$.posts[0].rejectRatio").value(0))
                .andExpect(jsonPath("$.posts[0].createdDate").value("2021-08-12T12:00:00"))
                .andExpect(jsonPath("$.posts[0].voteDeadline").value("2021-08-13T12:00:00"))
                .andExpect(jsonPath("$.posts[1].id").value(1L))
                .andExpect(jsonPath("$.posts[1].name").value("name"))
                .andExpect(jsonPath("$.posts[1].title").value("????????? ?????? ???"))
                .andExpect(jsonPath("$.posts[1].productImageUrl").value("test.jpg"))
                .andExpect(jsonPath("$.posts[1].isVoted").value(false))
                .andExpect(jsonPath("$.posts[1].permitRatio").value(50))
                .andExpect(jsonPath("$.posts[1].rejectRatio").value(50))
                .andExpect(jsonPath("$.posts[1].createdDate").value("2021-08-12T12:00:00"))
                .andExpect(jsonPath("$.posts[1].voteDeadline").value("2021-08-13T12:00:00"))
                .andExpect(jsonPath("$.posts[2].id").value(2L))
                .andExpect(jsonPath("$.posts[2].name").value("name"))
                .andExpect(jsonPath("$.posts[2].title").value("?????? ?????? ?????? ???"))
                .andExpect(jsonPath("$.posts[2].productImageUrl").value("test.jpg"))
                .andExpect(jsonPath("$.posts[2].isVoted").value(false))
                .andExpect(jsonPath("$.posts[2].permitRatio").value(50))
                .andExpect(jsonPath("$.posts[2].rejectRatio").value(50))
                .andExpect(jsonPath("$.posts[2].createdDate").value("2021-08-12T12:00:00"))
                .andExpect(jsonPath("$.posts[3].id").value(5L))
                .andExpect(jsonPath("$.posts[3].name").value("name"))
                .andExpect(jsonPath("$.posts[3].title").value("???????????? ?????????"))
                .andExpect(jsonPath("$.posts[3].productImageUrl").value("test.jpg"))
                .andExpect(jsonPath("$.posts[3].isVoted").value(false))
                .andExpect(jsonPath("$.posts[3].permitRatio").value(50))
                .andExpect(jsonPath("$.posts[3].rejectRatio").value(50))
                .andExpect(jsonPath("$.posts[3].createdDate").value("2021-08-12T12:00:00"))
                .andExpect(jsonPath("$.posts[3].voteDeadline").value("2021-08-13T12:00:00"))
                .andExpect(jsonPath("$.posts[4].id").value(4L))
                .andExpect(jsonPath("$.posts[4].name").value("name"))
                .andExpect(jsonPath("$.posts[4].title").value("????????? ?????????"))
                .andExpect(jsonPath("$.posts[4].productImageUrl").value("test.jpg"))
                .andExpect(jsonPath("$.posts[4].isVoted").value(false))
                .andExpect(jsonPath("$.posts[4].permitRatio").value(50))
                .andExpect(jsonPath("$.posts[4].rejectRatio").value(50))
                .andExpect(jsonPath("$.posts[4].createdDate").value("2021-08-12T12:00:00"))
                .andExpect(jsonPath("$.posts[4].voteDeadline").value("2021-08-13T12:00:00"));
    }

}