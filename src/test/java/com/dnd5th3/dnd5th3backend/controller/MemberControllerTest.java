package com.dnd5th3.dnd5th3backend.controller;

import com.dnd5th3.dnd5th3backend.config.MockSecurityFilter;
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
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
@ActiveProfiles("h2")
@TestMethodOrder(value = MethodOrderer.OrderAnnotation.class)
class MemberControllerTest {

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


    @Order(1)
    @DisplayName("회원가입 API 테스트")
    @Test
    void signUpAPI() throws Exception {
        String email = "moomool@naver.com";
        String name = "MOOMOOL";
        String password= "1234";

        Map<String,Object> request = new HashMap<>();
        request.put("name",name);
        request.put("password",password);
        request.put("email",email);

        ResultActions actions = mvc.perform(RestDocumentationRequestBuilders.post("/api/v1/member")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding(Charsets.UTF_8.toString())
                .content(objectMapper.writeValueAsString(request)));

        actions
                .andExpect(status().isCreated())
                .andDo(print())
                .andDo(document("member/save",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        requestFields(
                                fieldWithPath("email").description("회원가입 이메일"),
                                fieldWithPath("name").description("회원가입 닉네임"),
                                fieldWithPath("password").description("회원가입 비밀번호")
                        ),
                        responseFields(
                                fieldWithPath("memberId").description("회원 ID(고유번호)"),
                                fieldWithPath("email").description("회원가입 이메일"),
                                fieldWithPath("name").description("회원가입 닉네임"),
                                fieldWithPath("memberType").description("회원 유형")
                        )
                ))
                .andExpect(jsonPath("$.email").value(email))
                .andExpect(jsonPath("$.name").value(name));
    }

    @Order(2)
    @DisplayName("AccessToken 재발급 API 테스트")
    @Test
    void reissueAPI() throws Exception {
        String email = "test@naver.com";
        String refreshToken= "eyJhbGciOiJIUzI1NiJ9.eyJjbGFpbSI6eyJyZWZyZXNoIjoiMzRhMjIxODEtN2VhZi00NGI4LTg5ZDYtY2ViNDc4MzI5NzFhIn0sImV4cCI6MTYzMTAzMDY2N30.im-tspukCr25HOFg61DRrNjYkJ4oVumjsdWHqnuDHyQ";

        Map<String,Object> request = new HashMap<>();
        request.put("email",email);
        request.put("refreshToken",refreshToken);

        ResultActions actions = mvc.perform(RestDocumentationRequestBuilders.put("/api/v1/member/token")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding(Charsets.UTF_8.toString())
                .content(objectMapper.writeValueAsString(request)));

        actions
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("member/reissue",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        requestFields(
                                fieldWithPath("email").description("이메일"),
                                fieldWithPath("refreshToken").description("리프레시 토큰")
                        ),
                        responseFields(
                                fieldWithPath("email").description("이메일"),
                                fieldWithPath("accessToken").description("재발급한 엑세스토큰")
                        )
                ))
                .andExpect(jsonPath("$.email").value(email))
                .andExpect(jsonPath("$.accessToken").exists());
    }

    @Order(3)
    @DisplayName("이메일 중복체크 API 테스트")
    @Test
    void existsEmailAPI() throws Exception {
        String email = "test@naver.com";

        ResultActions actions = mvc.perform(RestDocumentationRequestBuilders.get("/api/v1/member/exists/{email}/email",email)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding(Charsets.UTF_8.toString()));

        actions
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("member/exists/email",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        pathParameters(
                                parameterWithName("email").description("이메일")
                        ),
                        responseBody()
                )).andReturn();
    }

    @Order(4)
    @DisplayName("닉네임 중복체크 API 테스트")
    @Test
    void existsNameAPI() throws Exception {
        String name = "moomool";

        ResultActions actions = mvc.perform(RestDocumentationRequestBuilders.get("/api/v1/member/exists/{name}/name",name)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding(Charsets.UTF_8.toString()));

        actions
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("member/exists/name",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        pathParameters(
                                parameterWithName("name").description("닉네임")
                        ),
                        responseBody()
                ));
    }

    @Order(5)
    @DisplayName("프로필 변경 API 테스트")
    @Test
    void updateProfileAPI() throws Exception {
        String name = "moo";
        String password= "12345";

        Map<String,Object> request = new HashMap<>();
        request.put("name",name);
        request.put("password",password);

        ResultActions actions = mvc.perform(RestDocumentationRequestBuilders.put("/api/v1/member")
                .principal(new UsernamePasswordAuthenticationToken(member,null))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding(Charsets.UTF_8.toString())
                .content(objectMapper.writeValueAsString(request)));

        actions
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("member/update",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        requestFields(
                                fieldWithPath("name").description("변경될 닉네임"),
                                fieldWithPath("password").description("변경될 비밀번호")
                        ),
                        responseFields(
                                fieldWithPath("memberId").description("회원 ID(고유번호)"),
                                fieldWithPath("email").description("이메일"),
                                fieldWithPath("name").description("닉네임"),
                                fieldWithPath("memberType").description("회원 유형")
                        )
                ))
                .andExpect(jsonPath("$.name").value(name));
    }

    @Order(6)
    @DisplayName("비밀번호 확인 API 테스트")
    @Test
    void checkPasswordAPI() throws Exception {
        String password= "12345";

        Map<String,Object> request = new HashMap<>();
        request.put("password",password);

        ResultActions actions = mvc.perform(RestDocumentationRequestBuilders.post("/api/v1/member/check/password")
                .principal(new UsernamePasswordAuthenticationToken(member,null))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding(Charsets.UTF_8.toString())
                .content(objectMapper.writeValueAsString(request)));

        actions
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("member/check/password",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        requestFields(
                                fieldWithPath("password").description("요청 비밀번호")
                        ),
                        responseBody()
                ));
    }

    @Order(7)
    @DisplayName("회원 탈퇴 API 테스트")
    @Test
    void withdrawalAPI() throws Exception {
        String email = "test@gmail.com";

        Map<String,Object> request = new HashMap<>();
        request.put("email",email);

        ResultActions actions = mvc.perform(RestDocumentationRequestBuilders.delete("/api/v1/member")
                .principal(new UsernamePasswordAuthenticationToken(member,null))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding(Charsets.UTF_8.toString())
                .content(objectMapper.writeValueAsString(request)));

        actions
                .andExpect(status().isAccepted())
                .andDo(print())
                .andDo(document("member/delete",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        requestFields(
                                fieldWithPath("email").description("탈퇴하는 회원 이메일")
                        ),
                        responseFields(
                                fieldWithPath("memberId").description("회원 ID(고유번호)"),
                                fieldWithPath("email").description("회원가입 이메일"),
                                fieldWithPath("name").description("회원가입 닉네임"),
                                fieldWithPath("memberType").description("회원 유형")
                        )
                ))
                .andExpect(jsonPath("$.email").value(email));
    }
}