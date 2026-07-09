package com.mehmetkatr.financehub.controller;

import com.mehmetkatr.financehub.dto.RegisterRequest;
import com.mehmetkatr.financehub.dto.UserResponse;
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
    public ResponseEntity<UserResponse> createUser(@RequestBody RegisterRequest request){

        User newUser = userService.registerUser(
                request.getFullName(),
                request.getEmail(),
                request.getPassword(),
                request.getPhone()
        );

        UserResponse wrappedUser = new UserResponse();
        wrappedUser.setEmail(newUser.getEmail());
        wrappedUser.setFullName(newUser.getFullName());
        wrappedUser.setPhone(newUser.getPhone());
        wrappedUser.setActive(newUser.getIsActive());

        return ResponseEntity.ok(wrappedUser);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable Long id){
        Optional<User> userOptional = userService.findById(id);

        if (userOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        User user = userOptional.get();

        UserResponse wrappedUser = new UserResponse();
        wrappedUser.setEmail(user.getEmail());
        wrappedUser.setFullName(user.getFullName());
        wrappedUser.setPhone(user.getPhone());
        wrappedUser.setActive(user.getIsActive());

        return ResponseEntity.ok(wrappedUser);
    }



}
