package com.mehmetkatr.user_service.service;

import com.mehmetkatr.user_service.dto.response.UserResponse;
import com.mehmetkatr.user_service.entity.User;
import com.mehmetkatr.user_service.exception.ResourceAlreadyExistsException;
import com.mehmetkatr.user_service.repository.UserRepository;
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
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock UserRepository userRepository;
    @Mock PasswordEncoder passwordEncoder;
    @Mock TokenService tokenService;
    @InjectMocks UserService userService;

    @Test
    void registerUser_yeni_kullanici_olusturur() {
        when(userRepository.findByEmail("a@x.com")).thenReturn(Optional.empty());
        when(userRepository.findByPhone("+905550001122")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("1234")).thenReturn("HASH");
        when(userRepository.save(any(User.class))).thenAnswer(inv -> {
            User u = inv.getArgument(0);
            u.setId(1L);
            return u;
        });

        UserResponse resp = userService.registerUser("Ali Veli", "a@x.com", "1234", "+905550001122");

        assertThat(resp.getId()).isEqualTo(1L);
        assertThat(resp.getEmail()).isEqualTo("a@x.com");
        assertThat(resp.getFullName()).isEqualTo("Ali Veli");
    }

    @Test
    void registerUser_var_olan_email_hata_verir() {
        when(userRepository.findByEmail("a@x.com"))
                .thenReturn(Optional.of(User.builder().id(1L).email("a@x.com").build()));

        assertThatThrownBy(() ->
                userService.registerUser("Ali", "a@x.com", "1234", "+905550001122"))
                .isInstanceOf(ResourceAlreadyExistsException.class)
                .hasMessageContaining("Email already exists");
    }

    @Test
    void loginUser_dogru_sifre_token_doner() {
        User user = User.builder().id(1L).email("a@x.com").fullName("Ali").passwordHash("HASH").build();
        when(userRepository.findByEmail("a@x.com")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("1234", "HASH")).thenReturn(true);
        when(tokenService.generateToken("a@x.com", "Ali")).thenReturn("JWT-TOKEN");

        String token = userService.loginUser("a@x.com", "1234");

        assertThat(token).isEqualTo("JWT-TOKEN");
    }

    @Test
    void loginUser_yanlis_sifre_hata_verir() {
        User user = User.builder().id(1L).email("a@x.com").passwordHash("HASH").build();
        when(userRepository.findByEmail("a@x.com")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("yanlis", "HASH")).thenReturn(false);

        assertThatThrownBy(() -> userService.loginUser("a@x.com", "yanlis"))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Invalid email or password");
    }
}
