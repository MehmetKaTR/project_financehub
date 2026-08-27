package com.mehmetkatr.user_service.controller;

import com.mehmetkatr.user_service.dto.request.LoginRequest;
import com.mehmetkatr.user_service.dto.response.LoginResponse;
import com.mehmetkatr.user_service.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request){
        LoginResponse response = new LoginResponse();
        response.setToken(userService.loginUser(request.getEmail(), request.getPassword()));
        return ResponseEntity.ok(response);
    }
}