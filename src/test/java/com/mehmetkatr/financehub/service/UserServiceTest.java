package com.mehmetkatr.financehub.service;

import com.mehmetkatr.financehub.dto.response.UserResponse;
import com.mehmetkatr.financehub.entity.User;
import com.mehmetkatr.financehub.exception.ResourceAlreadyExistsException;
import com.mehmetkatr.financehub.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private TokenService tokenService;

    @InjectMocks
    private UserService userService;

    @Test
    void registerUser_emailZatenVarsa_exceptionFirlatir() {
        when(userRepository.findByEmail("a@a.com")).thenReturn(Optional.of(new User()));

        assertThatThrownBy(() -> userService.registerUser("Ad Soyad", "a@a.com", "123456", "5551112233"))
                .isInstanceOf(ResourceAlreadyExistsException.class)
                .hasMessage("Email already exists");
    }

    @Test
    void registerUser_telefonZatenVarsa_exceptionFirlatir() {
        when(userRepository.findByEmail("a@a.com")).thenReturn(Optional.empty());
        when(userRepository.findByPhone("5551112233")).thenReturn(Optional.of(new User()));

        assertThatThrownBy(() -> userService.registerUser("Ad Soyad", "a@a.com", "123456", "5551112233"))
                .isInstanceOf(ResourceAlreadyExistsException.class)
                .hasMessage("Phone already exists");
    }

    @Test
    void registerUser_basarili_UserResponseDoner() {
        when(userRepository.findByEmail("a@a.com")).thenReturn(Optional.empty());
        when(userRepository.findByPhone("5551112233")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("123456")).thenReturn("hashed");

        User savedUser = User.builder()
                .id(1L)
                .fullName("Ad Soyad")
                .email("a@a.com")
                .phone("5551112233")
                .isActive(true)
                .build();
        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        UserResponse sonuc = userService.registerUser("Ad Soyad", "a@a.com", "123456", "5551112233");

        assertThat(sonuc.getEmail()).isEqualTo("a@a.com");
        assertThat(sonuc.getFullName()).isEqualTo("Ad Soyad");
    }

    @Test
    void loginUser_emailBulunamazsa_exceptionFirlatir() {
        when(userRepository.findByEmail("a@a.com")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.loginUser("a@a.com", "123456"))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Invalid email or password");
    }

    @Test
    void loginUser_sifreYanlissa_exceptionFirlatir() {
        User user = User.builder().email("a@a.com").passwordHash("hashed").build();
        when(userRepository.findByEmail("a@a.com")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("yanlis", "hashed")).thenReturn(false);

        assertThatThrownBy(() -> userService.loginUser("a@a.com", "yanlis"))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Invalid email or password");
    }

    @Test
    void loginUser_dogruBilgiler_tokenDoner() {
        User user = User.builder().email("a@a.com").fullName("Ad Soyad").passwordHash("hashed").build();
        when(userRepository.findByEmail("a@a.com")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("123456", "hashed")).thenReturn(true);
        when(tokenService.generateToken("a@a.com", "Ad Soyad")).thenReturn("token123");

        String sonuc = userService.loginUser("a@a.com", "123456");

        assertThat(sonuc).isEqualTo("token123");
    }
}
