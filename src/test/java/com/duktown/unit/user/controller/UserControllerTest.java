package com.duktown.unit.user.controller;

import com.duktown.domain.emailCert.dto.EmailCertDto;
import com.duktown.domain.user.controller.UserController;
import com.duktown.domain.user.dto.UserDto;
import com.duktown.domain.user.service.UserService;
import com.duktown.global.config.SecurityConfig;
import com.duktown.global.security.filter.JwtAuthorizationFilter;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@MockBean(JpaMetamodelMappingContext.class)
@WebMvcTest(
        controllers = UserController.class,
        excludeFilters = @ComponentScan.Filter(
                type = FilterType.ASSIGNABLE_TYPE,
                classes = {
                        SecurityConfig.class,
                        JwtAuthorizationFilter.class
                }
        )
)
public class UserControllerTest {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    // TODO: 이메일 인증 테스트 추가

    @DisplayName("아이디 중복 체크 성공")
    @WithMockUser
    @Test
    void loginIdDuplicateCheck_success() throws Exception {
        // given
        final String url = "/auth/id-duplicate";
        final String loginId = "dskim";
        UserDto.IdCheckRequest request = new UserDto.IdCheckRequest(loginId);
        String body = new ObjectMapper().writeValueAsString(request);   // serialize

        // when & then
        mockMvc.perform(post(url)
                        .contentType(APPLICATION_JSON_VALUE)
                        .content(body)
                        .with(csrf()))  // csrf token
                .andExpect(status().isOk())
                .andDo(print());    // 처리 내용 출력
    }

    @DisplayName("아이디 중복 체크 api에서 requestBody가 없으면 실패")
    @WithMockUser
    @Test
    void loginIdDuplicateCheck_noBody_fail() throws Exception {
        // given
        final String url = "/auth/id-duplicate";

        // when & then
        mockMvc.perform(post(url)
                        .with(csrf()))
                .andExpect(status().isInternalServerError())
                .andDo(print());
    }

    @DisplayName("아이디 중복 체크 api에서 아이디 입력하지 않으면 실패")
    @WithMockUser
    @Test
    void loginIdDuplicateCheck_noLoginId_fail() throws Exception {
        // given
        final String url = "/auth/id-duplicate";
        UserDto.IdCheckRequest request = new UserDto.IdCheckRequest();
        String body = new ObjectMapper().writeValueAsString(request);

        // when & then
        mockMvc.perform(post(url)
                        .contentType(APPLICATION_JSON_VALUE)
                        .content(body)
                        .with(csrf()))
                .andExpect(status().is4xxClientError())
                .andDo(print());
    }

    @DisplayName("회원가입 성공")
    @WithMockUser
    @Test
    void signup_success() throws Exception {
        // given
        final String url = "/auth/signup";
        UserDto.SignupRequest request = new UserDto.SignupRequest("dskim@duksung.ac.kr", "dskim", "1234", "김덕성");
        String body = new ObjectMapper().writeValueAsString(request);

        // when & then
        mockMvc.perform(post(url)
                        .contentType(APPLICATION_JSON_VALUE)
                        .content(body)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @DisplayName("회원가입 api에서 requestBody가 없으면 실패")
    @WithMockUser
    @Test
    void signup_noRequestBody_fail() throws Exception {
        // given
        final String url = "/auth/signup";

        // when & then
        mockMvc.perform(post(url)
                        .with(csrf()))
                .andExpect(status().isInternalServerError())
                .andDo(print());
    }

    // 다음 경우의 수는 작성하지 않았음
//    @DisplayName("회원가입 api에서 이메일 중복 시 실패")
//    @DisplayName("회원가입 api에서 이메일 입력 x시 실패")
//    @DisplayName("회원가입 api에서 이메일 유효하지 않은 경우 실패")
//    @DisplayName("회원가입 api에서 덕성 이메일 아닐 경우 실패")
//    @DisplayName("회원가입 api에서 아이디 중복 시 실패")
//    @DisplayName("회원가입 api에서 아이디 입력 x시 실패")
//    @DisplayName("회원가입 api에서 비밀번호 입력 x시 실패")

}
