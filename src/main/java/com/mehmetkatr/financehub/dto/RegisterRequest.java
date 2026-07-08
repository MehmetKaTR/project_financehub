package com.mehmetkatr.financehub.dto;

import lombok.Data;

@Data
public class RegisterRequest {
    private String email;
    private String fullName;
    private String password;
    private String phone;
    private boolean isActive = true;
}
