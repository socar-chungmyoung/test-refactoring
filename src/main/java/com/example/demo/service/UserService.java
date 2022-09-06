package com.example.demo.service;

import com.example.demo.domain.SignUpRequest;
import com.example.demo.domain.User;
import com.example.demo.exception.BadRequestException;
import com.example.demo.exception.ConflictException;
import com.example.demo.repository.UserRepository;
import com.example.demo.utils.CryptoUtil;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserService {
    public static final String FAKE_JWT_TOKEN = "fakeJwtToken";
    private UserRepository userRepository;
    private CryptoUtil cryptoUtil;
    public void signUp(SignUpRequest signUpRequest) {
        String username = signUpRequest.getUsername();
        String password = cryptoUtil.encryptPassword(signUpRequest.getPassword());

        if (username == null || username.length() < 1 || username.length() > 10) {
            throw new BadRequestException("username must be 1 ~ 10 length.");
        }

        User user = userRepository.findOneUser(username);
        if (user != null) {
            throw new ConflictException("User has been already registered...");
        }

        userRepository.insert(new User(username, password));
    }

    public String login(String username, String password) {
        User user = userRepository.findOneUser(username);
        if (!cryptoUtil.validatePassword(password, user.getPassword())) {
            throw new RuntimeException();
        }

        return FAKE_JWT_TOKEN;
    }
}
