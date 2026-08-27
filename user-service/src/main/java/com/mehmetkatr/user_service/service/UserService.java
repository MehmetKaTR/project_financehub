package com.mehmetkatr.user_service.service;

import com.mehmetkatr.user_service.dto.response.UserResponse;
import com.mehmetkatr.user_service.entity.User;
import com.mehmetkatr.user_service.exception.ResourceAlreadyExistsException;
import com.mehmetkatr.user_service.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;

    public Optional<UserResponse> findByEmail(String email) {
        return userRepository.findByEmail(email).map(this::toResponse);
    }

    public Optional<UserResponse> findByFullName(String fullName) {
        return userRepository.findByFullName(fullName).map(this::toResponse);
    }

    public Optional<UserResponse> findById(Long id) {
        return userRepository.findById(id).map(this::toResponse);
    }

    public Optional<UserResponse> findByPhone(String phone) {
        return userRepository.findByPhone(phone).map(this::toResponse);
    }

    @Transactional
    public UserResponse registerUser(String fullName, String email, String rawPassword, String phone) {
        if (userRepository.findByEmail(email).isPresent()) {
            throw new ResourceAlreadyExistsException("Email already exists");
        }

        if (userRepository.findByPhone(phone).isPresent()) {
            throw new ResourceAlreadyExistsException("Phone already exists");
        }

        User user = User.builder()
                .fullName(fullName)
                .email(email)
                .passwordHash(passwordEncoder.encode(rawPassword))
                .phone(phone)
                .isActive(true)
                .build();

        User savedUser = userRepository.save(user);

        return toResponse(savedUser);
    }

    @Transactional
    public String loginUser(String email, String rawPassword){

        Optional<User> existingUser = userRepository.findByEmail(email);
        if(!existingUser.isPresent())
            throw new RuntimeException("Invalid email or password");

        User user = existingUser.get();

        if(!passwordEncoder.matches(rawPassword, user.getPasswordHash()))
            throw new RuntimeException("Invalid email or password");

        return tokenService.generateToken(user.getEmail(), user.getFullName());
    }

    @Transactional
    public User registerOrLoginSocial(String fullName, String email, String phone) {
        Optional<User> existingUser = userRepository.findByEmail(email);
        if (existingUser.isPresent()) {
            return existingUser.get();
        }

        User user = User.builder()
                .fullName(fullName)
                .email(email)
                .passwordHash("SOCIAL_AUTH_")
                .phone(phone)
                .isActive(true)
                .build();

        User savedUser = userRepository.save(user);

        tokenService.generateToken(savedUser.getEmail(), savedUser.getFullName());

        return savedUser;
    }

    private UserResponse toResponse(User user) {
        UserResponse response = new UserResponse();
        response.setId(user.getId());
        response.setEmail(user.getEmail());
        response.setFullName(user.getFullName());
        response.setPhone(user.getPhone());
        response.setActive(user.getIsActive());

        return response;
    }
}