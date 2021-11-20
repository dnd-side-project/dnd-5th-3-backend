package com.dnd5th3.dnd5th3backend.controller;

import com.dnd5th3.dnd5th3backend.config.MockSecurityFilter;
import com.dnd5th3.dnd5th3backend.config.QuerydslConfig;
import com.dnd5th3.dnd5th3backend.controller.dto.mypage.InfoResponseDto;
import com.dnd5th3.dnd5th3backend.controller.dto.mypage.SortType;
import com.dnd5th3.dnd5th3backend.controller.dto.post.PostsListDto;
import com.dnd5th3.dnd5th3backend.domain.member.Member;
import com.dnd5th3.dnd5th3backend.domain.member.MemberType;
import com.dnd5th3.dnd5th3backend.domain.member.Role;
import com.dnd5th3.dnd5th3backend.domain.posts.Posts;
import com.dnd5th3.dnd5th3backend.domain.vote.Vote;
import com.dnd5th3.dnd5th3backend.domain.vote.VoteType;
import com.dnd5th3.dnd5th3backend.service.MyPageService;
import com.dnd5th3.dnd5th3backend.service.PostsService;
import com.google.common.base.Charsets;
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

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.dnd5th3.dnd5th3backend.utils.ApiDocumentUtils.getDocumentRequest;
import static com.dnd5th3.dnd5th3backend.utils.ApiDocumentUtils.getDocumentResponse;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureRestDocs
@ExtendWith({SpringExtension.class, RestDocumentationExtension.class})
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@ActiveProfiles("env")
class MyPageControllerTest {

    @Autowired
    private MockMvc mvc;
    @Autowired
    private WebApplicationContext context;
    @MockBean
    private MyPageService myPageService;

    private Member member;
    private Posts posts;
    private List<Posts> postsList;

    @BeforeEach
    void setUp(RestDocumentationContextProvider restDocumentation) {
        mvc = MockMvcBuilders.webAppContextSetup(context)
                .apply(documentationConfiguration(restDocumentation))
                .apply(springSecurity(new MockSecurityFilter()))
                .build();
        member = Member.builder().email("test@gmail.com").password("1234").role(Role.ROLE_USER).name("name")
                .build();
        posts = Posts.builder().id(1L).member(member).title("test").productImageUrl("test.jpg")
                .isVoted(false).permitCount(2).rejectCount(8)
                .voteDeadline(LocalDateTime.of(2021, 8, 27, 12, 0, 0))
                .build();
        posts.setCreatedDate(LocalDateTime.of(2021, 8, 26, 12, 0, 0));
        postsList = new ArrayList<>();
        postsList.add(posts);
    }

    @DisplayName("사용자의 이름, 이메일, 정렬된 게시글 목록을 반환한다.")
    @Test
    void getInfoApiTest() throws Exception {
        //given
        PostsListDto postsListDto = PostsListDto.builder().id(posts.getId()).name(posts.getMember().getName())
                .title(posts.getTitle()).productImageUrl(posts.getProductImageUrl())
                .isVoted(posts.getIsVoted()).permitRatio(20L).rejectRatio(80L)
                .createdDate(LocalDateTime.of(2021, 8, 26, 12, 00, 00))
                .voteDeadline(LocalDateTime.of(2021, 8, 27, 12, 00, 00))
                .build();
        List<PostsListDto> postsList =new ArrayList<>();
        postsList.add(0, postsListDto);

        InfoResponseDto responseDto = InfoResponseDto.builder()
                .name(member.getName()).email(member.getEmail()).postsList(postsList).build();
        given(myPageService.findMemberInfoWithSortType(member, SortType.WRITTEN.getValue())).willReturn(responseDto);

        //when
        ResultActions result = mvc.perform(RestDocumentationRequestBuilders.get("/api/v1/mypage?sorted=written")
                .principal(new UsernamePasswordAuthenticationToken(member, null))
        );

        //then
        result
                .andDo(print())
                .andDo(document("mypage",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        requestParameters(
                                parameterWithName("sorted").description("정렬 방법")
                        ),
                        responseFields(
                                fieldWithPath("name").description("사용자 이름"),
                                fieldWithPath("email").description("사용자 이메일"),
                                fieldWithPath("postsList.[].id").description("게시글 id"),
                                fieldWithPath("postsList.[].name").description("작성자 이름"),
                                fieldWithPath("postsList.[].title").description("글 제목"),
                                fieldWithPath("postsList.[].productImageUrl").description("상품 이미지"),
                                fieldWithPath("postsList.[].isVoted").description("투표 종료 여부"),
                                fieldWithPath("postsList.[].permitRatio").description("찬성 투표 비율"),
                                fieldWithPath("postsList.[].rejectRatio").description("반대 투표 비율"),
                                fieldWithPath("postsList.[].createdDate").description("작성된 시간"),
                                fieldWithPath("postsList.[].voteDeadline").description("투표 종료 시간")
                        )
                ))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("name"))
                .andExpect(jsonPath("$.email").value("test@gmail.com"))
                .andExpect(jsonPath("$.postsList[0].id").value(1L))
                .andExpect(jsonPath("$.postsList[0].name").value("name"))
                .andExpect(jsonPath("$.postsList[0].title").value("test"))
                .andExpect(jsonPath("$.postsList[0].productImageUrl").value("test.jpg"))
                .andExpect(jsonPath("$.postsList[0].isVoted").value(false))
                .andExpect(jsonPath("$.postsList[0].permitRatio").value(20L))
                .andExpect(jsonPath("$.postsList[0].rejectRatio").value(80L))
                .andExpect(jsonPath("$.postsList[0].createdDate").value("2021-08-26T12:00:00"))
                .andExpect(jsonPath("$.postsList[0].voteDeadline").value("2021-08-27T12:00:00"));
    }
}