package com.mehmetkatr.user_service.dto.request;

import lombok.Data;

@Data
public class RegisterRequest {
    private String email;
    private String fullName;
    private String password;
    private String phone;
    private boolean isActive = true;
}
