package com.mehmetkatr.user_service.repository;

import com.mehmetkatr.user_service.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    Optional<User> findByFullName(String fullName);

    Optional<User> findByPhone(String phone);
}