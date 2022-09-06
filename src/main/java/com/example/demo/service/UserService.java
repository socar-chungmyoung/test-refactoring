package com.example.demo.service;

import com.example.demo.domain.SignUpRequest;
import com.example.demo.domain.User;
import com.example.demo.exception.BadRequestException;
import com.example.demo.exception.ConflictException;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    public static final String FAKE_JWT_TOKEN = "fakeJwtToken";
    @Autowired
    private UserRepository userRepository;

    public void signUp(SignUpRequest signUpRequest) {
        String username = signUpRequest.getUsername();
        String password = signUpRequest.getPassword();

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
        if (!user.getPassword().equals(password)) {
            throw new RuntimeException();
        }

        return FAKE_JWT_TOKEN;
    }
}
