package com.mehmetkatr.financehub.dto;

import lombok.Data;

@Data
public class UserResponse {
    private String email;
    private String fullName;
    private String phone;
    private boolean isActive;
}
