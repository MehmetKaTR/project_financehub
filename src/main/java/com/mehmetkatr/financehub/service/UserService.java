package com.mehmetkatr.financehub.service;

import com.mehmetkatr.financehub.entity.User;
import com.mehmetkatr.financehub.repository.UserRepository;
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

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public Optional<User> findByFullName(String fullName) {
        return userRepository.findByFullName(fullName);
    }

    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    public Optional<User> findByPhone(String phone) {
        return userRepository.findByPhone(phone);
    }

    public User registerUser(String fullName, String email, String rawPassword, String phone) {
        if (userRepository.findByEmail(email).isPresent()) {
            throw new RuntimeException("Email already exists");
        }

        if (userRepository.findByPhone(phone).isPresent()) {
            throw new RuntimeException("Phone already exists");
        }

        User user = User.builder()
                .fullName(fullName)
                .email(email)
                .passwordHash(passwordEncoder.encode(rawPassword))
                .phone(phone)
                .isActive(true)
                .build();

        User savedUser = userRepository.save(user);

        String jwt = tokenService.generateToken(savedUser.getEmail(), savedUser.getFullName());
        System.out.println("Generated JWT for new user: " + jwt);

        return savedUser;
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

}
