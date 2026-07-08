package com.mehmetkatr.financehub.controller;

import com.mehmetkatr.financehub.dto.RegisterRequest;
import com.mehmetkatr.financehub.entity.User;
import com.mehmetkatr.financehub.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<User> createUser(@RequestBody RegisterRequest request){

        User newUser = userService.registerUser(
                request.getFullName(),
                request.getEmail(),
                request.getPassword(),
                request.getPhone()
        );

        return ResponseEntity.ok(newUser);

    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id){
        Optional<User> user = userService.findById(id);

        return user.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }




}
