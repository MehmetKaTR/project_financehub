package com.mehmetkatr.user_service.dto.response;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserResponse implements Serializable{
    private Long id;
    private String email;
    private String fullName;
    private String phone;
    private boolean isActive;
}