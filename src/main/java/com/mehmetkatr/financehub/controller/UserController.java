package com.mehmetkatr.financehub.controller;

import com.mehmetkatr.financehub.dto.request.RegisterRequest;
import com.mehmetkatr.financehub.dto.response.UserResponse;
import com.mehmetkatr.financehub.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<UserResponse> createUser(@RequestBody RegisterRequest request){

        UserResponse newUser = userService.registerUser(
                request.getFullName(),
                request.getEmail(),
                request.getPassword(),
                request.getPhone()
        );

        return ResponseEntity.ok(newUser);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable Long id){
        return userService.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
