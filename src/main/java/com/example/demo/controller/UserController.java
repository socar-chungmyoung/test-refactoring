package com.example.demo.controller;

import com.example.demo.domain.SignUpRequest;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

@RestController
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public void doLogin(HttpServletResponse httpServletResponse, @RequestParam String username, @RequestParam String password) {
        String jwtToken = userService.login(username, password);
        httpServletResponse.setHeader("Authorization", jwtToken);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/users")
    public void signUp(@RequestBody SignUpRequest signUpRequest) {
        userService.signUp(signUpRequest);
    }
}
