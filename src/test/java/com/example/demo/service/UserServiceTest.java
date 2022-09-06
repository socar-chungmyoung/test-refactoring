package com.example.demo.service;

import com.example.demo.domain.SignUpRequest;
import com.example.demo.domain.User;
import com.example.demo.exception.BadRequestException;
import com.example.demo.exception.ConflictException;
import com.example.demo.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @InjectMocks
    private UserService userService;
    @Mock
    private UserRepository userRepository;

    @Test
    public void login_method는_username과_password를_받아서_DB에_들어있는_패스워드와_일치할경우_FAKE_JWT_TOKEN을_반환한다() {
        // given
        String username = "kai";
        String password = "1111111";

        when(userRepository.findOneUser("kai"))
                .thenReturn(new User(username, password));

        // when
        String jwtToken = userService.login(username, password);

        // then
        assertThat(jwtToken).isEqualTo("fakeJwtToken");
    }

    @Test
    public void username_validation이_통과될수_있고_DB에_유저가_존재하지_않으면_DB에_유저가_저장되어야_한다() {
        // given
        SignUpRequest signUpRequest = createKai();

        when(userRepository.findOneUser("kai"))
                .thenReturn(null);

        // when
        userService.signUp(signUpRequest);

        // then
        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        verify(userRepository, times(1))
                .insert(captor.capture());

        assertThat(captor.getValue()).isNotNull();
        assertThat(captor.getValue().getUsername()).isEqualTo("kai");
    }

    @Test
    public void username_validation이_통과했을경우_DB에서_유저를_찾아보고_이미_존재할경우_ConflictException을_던져야한다() {
        // given
        SignUpRequest signUpRequest = createKai();

        when(userRepository.findOneUser("kai"))
                .thenReturn(new User("dummyUser", "password"));

        // expect
        Assertions.assertThrows(ConflictException.class, () -> {
            // when
            userService.signUp(signUpRequest);
        });
    }

    private SignUpRequest createKai() {
        String userName = "kai";
        String password = "1234";

        SignUpRequest signUpRequest = new SignUpRequest();
        signUpRequest.setUsername(userName);
        signUpRequest.setPassword(password);

        return signUpRequest;
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "kaaaaaaaaaaaaai"})
    public void username이_유효하지_않은_이름인_경우_BadRequestException을_던저야_한다(String name) {
        // given
        SignUpRequest kaiWithUnValidName = createKai();
        kaiWithUnValidName.setUsername(name);

        // expect
        Assertions.assertThrows(BadRequestException.class, () -> {
            // when
            userService.signUp(kaiWithUnValidName);
        });
    }

    @Test
    public void username이_null일_경우_BadRequestException을_던저야_한다() {
        // given
        SignUpRequest kaiWithNullName = createKai();
        kaiWithNullName.setUsername(null);

        // expect
        Assertions.assertThrows(BadRequestException.class, () -> {
            // when
            userService.signUp(kaiWithNullName);
        });
    }
}
