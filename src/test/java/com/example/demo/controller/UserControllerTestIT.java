package com.example.demo.controller;

import com.example.demo.domain.SignUpRequest;
import com.example.demo.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static com.example.demo.service.UserService.FAKE_JWT_TOKEN;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
public class UserControllerTestIT {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private UserService userService;

    @Test
    public void login_API는_username_password를_받아야하고_패스워드가_일치할_경우_response_header에_jwt_token을_반환해야한다() throws Exception {
        // given
        String username = "kai";
        String password = "124124124";

        when(userService.login(username, password))
                .thenReturn(FAKE_JWT_TOKEN);

        // when
        ResultActions perform = mockMvc
                .perform(
                        post("/login?username={username}&password={password}", username, password)
                                .contentType(MediaType.APPLICATION_JSON)
                );

        // then
        String token = perform
                .andReturn()
                .getResponse()
                .getHeader("Authorization");
        assertThat(token).isEqualTo(FAKE_JWT_TOKEN);
    }

    @Test
    public void signUp_API는_username_password_파라미터를_지녀야_하고_성공시_201_status_code를_반환한다() throws Exception {
        // given
        SignUpRequest signUpRequest = createSignUpRequest();
        ObjectMapper objectMapper = new ObjectMapper();

        // when
        ResultActions perform = mockMvc
                .perform(
                        post("/users")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(signUpRequest))
                );

        // then
        perform.andExpect(status().is(HttpStatus.CREATED.value()));
    }

    @NotNull
    private SignUpRequest createSignUpRequest() {
        String userName = "kai";
        String password = "1234";

        SignUpRequest signUpRequest = new SignUpRequest();
        signUpRequest.setUsername(userName);
        signUpRequest.setPassword(password);
        return signUpRequest;
    }
}
